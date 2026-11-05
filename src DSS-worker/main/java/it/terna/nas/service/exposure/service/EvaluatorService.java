package it.terna.nas.service.exposure.service;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.nas.service.dal.documents.PolicyEvaluatorContextDocument;
import it.terna.nas.service.dal.service.PolicyEvaluatorContextService;
import it.terna.nas.service.exposure.events.PolicyRequestEvent;
import it.terna.nas.service.material.converter.DefaultPolicyConverterResolver;
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.dto.policy.BinaryExpression;
import it.terna.nas.service.material.dto.policy.ExpressionContext;
import it.terna.nas.service.material.dto.policy.Policy;
import it.terna.nas.service.material.dto.policy.PolicyOperator;
import it.terna.nas.service.material.dto.policy.PolicyVariable;
import it.terna.nas.service.material.dto.policy.ValueHolder;
import it.terna.nas.service.material.dto.policy.types.PolicyValue;
import it.terna.nas.service.material.transformer.PolicyTransformer;
import it.terna.nas.service.material.utility.Catalog.VariableType;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class EvaluatorService {
	
	@Autowired
	private AssetDataSupplier dataSupplier;
	
	@Autowired
	private PolicyEvaluatorContextService policyEvaluatorContextService;
	
	@Autowired
	//private DefaultPolicyConverterResolver converterResolver;
	private DefaultPolicyConverterResolver converterResolver;
	
	@Value("${writePolicyContext:false}")
	private Boolean writePolicyContext;
	
	private ValueHolder<?> doubleValue(PolicyVariable variable){
		ValueHolder<Double> val = new ValueHolder<>(
				variable.getName(),
				null,
				variable.getRawValue() != null
					? Double.valueOf(variable.getRawValue())
					: null);
		
		return val;
	}
	
	private ValueHolder<?> stringValue(PolicyVariable variable){
		ValueHolder<String> val = new ValueHolder<>(
				variable.getName(),
				null,
				variable.getRawValue());
		
		return val;
	}
	

	private PolicyValue<?> resolveVariable(PolicyVariable variable,
										   AssetData asset,
										   PolicyValue<?> target) {
		
		Assert.notNull(variable, 	"variable is null");
		Assert.notNull(asset, 		"asset is null");
		
		PolicyValue<?> result = 
		switch (variable.getType()){
			case NUMERIC -> doubleValue(variable);
			case ALPHANUMERIC -> stringValue(variable);
			case DATE -> stringValue(variable);
			case ASSETSPEC,
			 	 MISURA,
			 	 VARIABILE,
			 	 HEALTH_INDEX,
			 	 TASSO_GUASTO,
			 	 TASSO_ANOMALIA,
			 	 ATTIVITA -> dataSupplier.getData(asset, 
											  	  variable.getName(), 
											  	  variable.getType(), 
											  	  null);
			case CATALOGO_DIFFORMITA, 
				 CATALOGO_INTERFERENZA -> dataSupplier.getData(asset, 
										  variable.getName(), 
										  variable.getType(),
										  target.getValue() != null
										  	? target.getValue()
										  	: null);
	
			case MODELLO -> dataSupplier.getData(asset, 
										  variable.getName(), 
										  variable.getType(),
										  variable.getRawValue());
			
			default -> throw new IllegalArgumentException("Unexpected value: " + variable.getType());
		};
		
		return result;
	}
	
	public Boolean evaluate(PolicyRequestEvent event, AssetData asset, Policy policy) 
	{
		long startTime = System.currentTimeMillis();
		log.debug("call evaluate({}, {}) |{}|", asset.getAsset().getAssetNum(), policy.getId(), startTime);
		
		if (policy.getHasInputs() == 0)
		{
		    log.debug("La Policy {} puo' non avere input.", policy.getCode());
		    return Boolean.TRUE;
		}
		
		Boolean result = null;
		Map<Integer, ExpressionContext> mapInputContext = null;
		try {
			// step1 get list of policyInput to calculate a boolean result
			List<BinaryExpression> expressions = policy.getInputs().stream()
					   .filter(x -> x instanceof BinaryExpression)
					   .map(x -> BinaryExpression.class.cast(x))
					   .sorted()
					   .collect(Collectors.toList());
			
			// step2 for each policyInput build a context to contain formula and variable value
			mapInputContext = expressions.stream()
										 .map(ExpressionContext::new)
										 .collect(Collectors.toMap(x-> x.getExpression().getAlias(),
																   x -> x));
			// step3 for each context retrieve value through AssetService
			mapInputContext.entrySet().forEach( entry -> {
				ExpressionContext ctx = entry.getValue();
				
				BinaryExpression expression = ctx.getExpression();
				
				if(expression.getRightVar() != null) {
					ctx.setTargetVal(resolveVariable(expression.getRightVar(), asset, null));
				}
				ctx.setLeftVal(resolveVariable(expression.getLeftVar(), asset, ctx.getTargetVal()));
				ctx.setResult(PolicyOperator.PARI_DISPARI == expression.getOperator()
													? evaluatePariOrDispari(ctx)
													: evaluateExpression(ctx));
			});
			
			// step4 evaluate the formula
			result = evaluateWholePolicy(policy.getFormula(), mapInputContext);
		} finally {
			
			if (writePolicyContext && mapInputContext != null)
				saveEvaluationContext(asset, policy, result, mapInputContext.values());
			
		}
		
		long endTime = System.currentTimeMillis();
		log.debug("evaluate({}, {}) ==> {} |{}| |{}|", asset.getAsset().getAssetNum(), policy.getId(), result, endTime, endTime - startTime);
		return result;
	}
	
	/**
	 * Save evaluation context on MongoDb
	 * @param asset
	 * @param policy
	 * @param policyResult
	 * @param ctxs
	 */
	private void saveEvaluationContext(AssetData asset, Policy policy, Boolean policyResult, Collection<ExpressionContext> ctxs) {
		log.debug("  - trying to save evaluation context for policy '{}' on asset '{}'", policy.getId(), 
				 asset.getAsset().getAssetNum());

		try {
			PolicyEvaluatorContextDocument document = new PolicyEvaluatorContextDocument();
			document.setId(UUID.randomUUID().toString());
			document.setAssetData(asset);
			document.setPolicy(policy);
			document.setCreationDate(Instant.now().getEpochSecond());
			
			String documentDay = Assorted.toString(new Date(), "yyyyMMddHHmmss");
			
			document.setDocumentDay(documentDay);
			
			document.setExpressions(PolicyTransformer.toPolicyExpressions(ctxs));
			document.setPolicyFormula(policy.getFormula());
			document.setPolicyResult(policyResult);	
			policyEvaluatorContextService.save(document);
		} catch (DataLayerException dle) {
			log.error("  - Error saving evaluation context for policy '{}' on asset '{}'\nException is: {}", policy.getId(), 
								   asset.getAsset().getAssetNum(), 
								   dle);
		}
	}

	
	private Boolean evaluateExpression(ExpressionContext ctx) {
		
		BinaryExpression expr = ctx.getExpression();
		String formula = ctx.writeFormula();
		
		log.debug(" -- Evaluating literal expression [{}] is '{}'", expr.getAlias(), formula);
		
		StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
		evaluationContext.setVariable(ExpressionContext.OPERAND_LEFT, ctx.getLeftVal());
		evaluationContext.setVariable(ExpressionContext.OPERAND_RIGHT, adjustTypes(ctx));
		
		log.debug(" -- Evaluating expression [{}] is '{}' ['{}' {} '{}'] ", expr.getAlias(), formula, 
				ctx.getLeftVal() != null 
				? ctx.getLeftVal().toString()
				: null,
				ctx.getExpression().getOperator().getSupportedSymbol(),
				ctx.getTargetVal() != null
				? ctx.getTargetVal().toString()
				: null);
		
		// Necessario per evitare che in caso di espressione null < numero risulti true
		PolicyOperator operator = expr.getOperator();
		if (ctx.getLeftVal() == null && (operator == PolicyOperator.EQUAL 
				|| operator == PolicyOperator.NOT_EQUAL
				|| operator == PolicyOperator.GREATER_THAN_EQUAL
				|| operator == PolicyOperator.GREATER_THAN
				|| operator == PolicyOperator.LESS_THAN
				|| operator == PolicyOperator.LESS_THAN_EQUAL)) 
		{
			log.debug(" -- Evaluated literal expression [{}] is '{}' result {}", expr.getAlias(), formula, false);
			return false;
		}
		
		if ((ctx.getLeftVal() == null || ctx.getLeftVal() != null && ctx.getLeftVal().getValue() == null) && 
		    (expr.getLeftVar().getType() == VariableType.VARIABILE || expr.getLeftVar().getType() == VariableType.MISURA) &&
		    (PolicyOperator.EXIST == expr.getOperator() || PolicyOperator.NOT_EXIST == expr.getOperator() || PolicyOperator.PARI == expr.getOperator() || PolicyOperator.DISPARI == expr.getOperator())) 
		{
		    log.debug(" -- Tipe Expression [{}], the operator {} value {} result {}", expr.getLeftVar().getType(), expr.getOperator(), ctx.getLeftVal(), false);
		    return false;
		}
		
		ExpressionParser parser = new SpelExpressionParser();
		Expression spelExpression = parser.parseExpression(formula);
		
		Boolean result = spelExpression.getValue(evaluationContext, Boolean.class);
		log.debug(" -- Evaluated literal expression [{}] is '{}' result {}", expr.getAlias(), formula, result);
		return result;
	}
	
	private PolicyValue<?> adjustTypes(ExpressionContext ctx) {
		if (ctx.getTargetVal() != null && ctx.getTargetVal().getValue() != null) {
			if (ctx.getLeftVal() != null && ctx.getLeftVal().getValue() != null) {
				// Controllo: targetVal è compatibile con leftVal?
				if (!ctx.getLeftVal().getValue().getClass().isAssignableFrom(ctx.getTargetVal().getValue().getClass())) {
					// Serve conversione del targetVal per adattarsi a leftVal
					return convert(ctx.getTargetVal(), ctx.getLeftVal().getValue().getClass());
				}
			} 
		} 
		return ctx.getTargetVal();
		
	}
	
	private <S,T> PolicyValue<T> convert(PolicyValue<S> policyValue, Class<T> targetClass){
		T converted = converterResolver.from(policyValue.getValue())
						   			   .to(targetClass);
		
		return new ValueHolder<T>(policyValue.getKey(), policyValue.getDescription(), converted);
	}
	
	private Boolean evaluatePariOrDispari(ExpressionContext ctx) {
		
		Assert.notNull(ctx, "context is null");
		Assert.notNull(ctx.getTargetVal(), "target val not found");
		
		BinaryExpression expr = ctx.getExpression();
		
		StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
		
		String targetString = (String) ctx.getTargetVal().getValue();
		evaluationContext.setVariable(ExpressionContext.OPERAND_RIGHT, targetString);
		
		Double value = (Double) ctx.getLeftVal().getValue();
		String verifyThat = value % 2 == 0 ? "PARI" : "DISPARI";
		
		evaluationContext.setVariable(ExpressionContext.OPERAND_LEFT, verifyThat);
		
		String formula = ctx.writeFormula();
		log.debug(" -- Evaluating literal expression [{}] is '{}'", expr.getAlias(), formula);
		
		log.debug(" -- Evaluating expression [{}] is '{}' ['{}' {} '{}'] ", expr.getAlias(), formula, verifyThat,
			ctx.getExpression().getOperator().getSupportedSymbol(),
			targetString);
	
		ExpressionParser parser = new SpelExpressionParser();
		Expression spelExpression = parser.parseExpression(formula);
		
		Boolean result = spelExpression.getValue(evaluationContext, Boolean.class);
		log.debug(" -- Evaluated literal expression [{}] is '{}' result {}", expr.getAlias(), formula, result);
		return result;
	}
	
	private Boolean evaluateWholePolicy(String rawFormula, Map<Integer, ExpressionContext> mapInputContext) {

		Assert.hasLength(rawFormula, "rawFormula is null or empty");
		
		// get entire expression from policy record;
		String formula = rawFormula;
		//String regexExactMatch = "\\b%d\\b";
		String regexExactMatch = Constants.FORMULA_ITEM_REGEX;
		// step6 replace numeric ids in the main formula with result at step5
		for(Entry<Integer, ExpressionContext> entry : mapInputContext.entrySet()) {
			formula = formula.replaceAll(String.format(regexExactMatch, entry.getKey()), entry.getValue().getResult().toString());
		}
		
		// replace boolean operator
		formula = formula.replace("AND", "&&")
						 .replace("OR", "||");
		
		log.info(String.format(" -- Evaluating final formula is '%s'", formula));
		ExpressionParser parser = new SpelExpressionParser();
		Expression expr = parser.parseExpression(formula);
		
		Boolean result = (Boolean) expr.getValue();
		
		return result;
	}
	
	
}
