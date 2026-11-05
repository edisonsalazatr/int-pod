package it.terna.nas.service.material.transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.nas.service.dal.documents.PolicyExpressionItem;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.entity.eam.X4PolicyInput;
import it.terna.nas.service.exceptions.UnsupportPolicyInputException;
import it.terna.nas.service.material.dto.policy.BinaryExpression;
import it.terna.nas.service.material.dto.policy.CombinationInput;
import it.terna.nas.service.material.dto.policy.ExpressionContext;
import it.terna.nas.service.material.dto.policy.Input;
import it.terna.nas.service.material.dto.policy.Policy;
import it.terna.nas.service.material.dto.policy.PolicyOperator;
import it.terna.nas.service.material.dto.policy.PolicyVariable;
import it.terna.nas.service.material.dto.policy.PolicyVariable.PolicyVariableBuilder;
import it.terna.nas.service.material.utility.Catalog.AttributeAssetType;
import it.terna.nas.service.material.utility.Catalog.PolicyInputType;
import it.terna.nas.service.material.utility.Catalog.PolicyTimeUnit;
import it.terna.nas.service.material.utility.Catalog.VariableType;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PolicyTransformer 
{
	/**
	 * must be treated as dates
	 */
	public static final Set<String> DATE_ATTRIBUTES = Set.of(AttributeAssetType.IN_FUNZIONE_DA.getName());
    
	public static Policy toDto(X4Policy entity, List<X4PolicyInput> listInput) 
	{
		if (entity == null)
			return null;
		
		Policy policy = new Policy();
		policy.setId(entity.getX4PolicyId());
		policy.setCode(entity.getIdPolicy());
		policy.setDescription(entity.getDescription());
		policy.setMacroActivityId(entity.getIdMacroAttivita());
		policy.setAssetType(entity.getTipologiaAsset());
		policy.setStatus(entity.getStatus());
		policy.setTipoPolicy(entity.getTipoPolicy());
		policy.setLogicaTronco(entity.getLogicaTronco() != null ? entity.getLogicaTronco() : 0);
		policy.setMaxAnticipo(entity.getMaxAnticipo() != null ? entity.getMaxAnticipo() : 0);
		policy.setMaxPosticipo(entity.getMaxPosticipo() != null ? entity.getMaxPosticipo() : 0);
		policy.setTimeUnit(PolicyTimeUnit.fromCode(entity.getX4RifTemporale()));
		policy.setDifferibilita(entity.getDifferibilita() != null ? entity.getDifferibilita() : 0);
		policy.setEleggibilita(entity.getEleggibilita() != null 
											? entity.getEleggibilita()
											: 0);
		policy.setFrequenza(entity.getFrequenza() != null ? entity.getFrequenza() : 0);	
		policy.setHasInputs(entity.getHasInputs());
		policy.setAPartireDa(entity.getAPartireDa() != null ? entity.getAPartireDa() : null);
		policy.setPolicyAggr(entity.getPolicyAggr() != null ? entity.getPolicyAggr() : 0);
		
		// set formula
		setFormula(policy, entity);
			
		
		if(Assorted.isNotEmpty(listInput)) 
		{
			policy.setListPolicyInput(listInput);	
			List<Input> list = listInput.stream()
										.map( x -> createInput(x))
										.collect(Collectors.toList());
						
			policy.setInputs(list);
			
		} else
		    policy.setInputs(new ArrayList<>());
		
		return policy;
	}
	
	private static void setFormula(Policy policy, X4Policy record) {
		String formula = Assorted.checkNullString(record.getCombInput(), true);
		if(Assorted.isNotEmpty(formula)) {
			policy.setFormula(formula);
		} else {
			log.info("  - No formula found for policy with id {}, setting formula to default", record.getX4PolicyId());
			policy.setFormula(String.format(Constants.FORMULA_ITEM_REGEX, 1)); 
		}
	} 
	
	public static Input createInput(X4PolicyInput entity) {
		
		PolicyInputType type = PolicyInputType.fromCode(entity.getTipologiaInput());
		
		if(type == null) {
			throw new UnsupportPolicyInputException("No PolicyInputType entry", entity.getTipologiaInput());
		}
	
		switch (type) {
		case ATTIVITA: 
			return attivitaExpression(entity);
		case ATTRIBUTO: 
			return attributeExpression(entity);
		case HI: 
			return healthIndexExpression(entity);
		case DIFFORMITA: 
			return difformitaExpression(entity);
		case INTERFERENZA: 
			return interferenzaExpression(entity);
		case VARIABILE: 
			return variableExpression(entity);
		case COMBINAZIONE : 
			return createCombination(entity);
		case TASSO_GUASTO: 
			return tassoGuastoExpression(entity);
		case TASSO_ANOMALIA: 
			return tassoAnomaliaExpression(entity);
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}
	
	/**
	 * Se TIPOLOGIAINPUT è valorizzato con ATTRIBUTO nella colonna 
	 * IDINPUT sarà riportato il nome della colonna ASSETATTRID della tabelle ASSETSPEC 
	 * questo stesso valore sarà presente in consistenza asset come una chiave della mappa chiave valore relativi 
	 * agli attributi asset. In questa mappa infatti, la chiave sarà costituita dalla colonna se presa dalla tabella ASSETSPEC,
	 * il valore corrisponderà al valore dell’attributo per quell’asset. 
	 * Nel caso l'attributo ammette un dominio di valori predefiniti per i quali è ammessa una tabella di gravità/priorità,
	 * il mapping dei possibili valori e priorità sarà recuperata dalle tabelle definite nel paragrafo 3.3.7. 
	 * Saranno riportate in memoria i dati delle tabelle del suddetto paragrafo per garantire l'eventuale trascodifica e 
	 * comparazione
	 * @param entity
	 * @return
	 */
	public static BinaryExpression attributeExpression(X4PolicyInput entity) 
	{
		// type of record
		PolicyInputType inputType = PolicyInputType.fromCode(entity.getTipologiaInput());
		
		// bean to retun
		BinaryExpression expression = new BinaryExpression();
		
		// data for audit and logging
		expression.setId(Long.valueOf(entity.getX4PolicyInputId()));
		expression.setType(inputType);
		
		// operator
		expression.setOperator(PolicyOperator.getBySymbol(entity.getOperatore()));
		
		// alias for replacing in policy formula
		expression.setAlias(entity.getNInput());
		
		// left operand
		expression.setLeftVar(new PolicyVariable(entity.getIdInput(), VariableType.ASSETSPEC));
		
		PolicyVariable rightVar = null;
		
		// right operand
		if (entity.getTargetNum() != null) {
			
			// VALORIZZATO TARGETNUM 
			rightVar =  new PolicyVariable.PolicyVariableBuilder()
					  .rawValue(String.valueOf(entity.getTargetNum()))
					  .type(VariableType.NUMERIC)
					  .build();
		} else {
			
		    if (Assorted.notNull(entity.getData()) && Assorted.isNotEmpty(entity.getTargetRaffronto()))
		    {    
		        rightVar = new PolicyVariable.PolicyVariableBuilder()
                                            .rawValue(entity.getTargetRaffronto())
                                            .type(VariableType.DATE)
                                            .build();
		    } else {
		        rightVar = new PolicyVariable.PolicyVariableBuilder()
					  		              .rawValue(entity.getTargetRaffronto())
					  		              .type(VariableType.ALPHANUMERIC)
					  		              .build();
		    }
		}

		expression.setRightVar(rightVar);
		
		return expression;
	}
	
	/**
	 * Se TIPOLOGIAINPUT è valorizzato con DIFFORMITA 
	 * i.	se la colonna IDMISURA non è valorizzata (null) nella colonna IDINPUT sarà riportato l'id difformità 
	 * (id tabella X4CATDIFFORMITA). Con tale chiave sarà ricercato nella mappa chiave valore relative alle difformità 
	 * associate all'asset il valore della difformità. Per questo tipo di condizione l'algoritmo prevede anche di recuperare 
	 * la tabella di mapping dei possibili valori di gravità associati al fine poi di comparare il livello di gravità.
	 * Se associati all'asset non sono presenti difformità il valore della condizione identificata dal valore del campo 
	 * NInput della singola riga della tabella X4POLICYINPUT corrisponderà a false. 
	 * 
	 * ii.	Se la colonna IDMISURA è valorizzata nella colonna nella colonna IDINPUT sarà riportato l'id difformità 
	 * (id tabella X4CATDIFFORMITA) in questo caso sarà necessario valutare il valore della variabile misura che
	 *  popola appunto il campo IDMISURA. Il valore corrisponderà al X4CATMISURA. 
	 *  Con questa chiave sarà ricercato nella mappa chiave/valore relativa alle misure associate all'asset il valore
	 *  della variabile. 
	 *  Se nel campo TARGETRAFFRONTONUM è valorizzato e TARGETRAFFRONTO è null il valore della variabile sarà comparata
	 *  con un numerico, se invece valorizzato il campo TARGETRAFFRONTO è valorizzato la chiave corrisponderà ad una 
	 *  chiave della tabella X4CATMODELLO. 
	 *  Sulla della tabella X4CATMODELLOASSET e dei valori dell'asset sarà identificato il modello soglia da recuperare 
	 *  e il valore riportato nella colonna N1
	 * @param entity
	 * @return
	 */
	public static BinaryExpression difformitaExpression(X4PolicyInput entity) {
				
		// bean to retun
		BinaryExpression expression = new BinaryExpression();
		
		// data for audit and logging
		expression.setId(Long.valueOf(entity.getX4PolicyInputId()));
		// type of record
		expression.setType(PolicyInputType.fromCode(entity.getTipologiaInput()));
		
		// operator
		expression.setOperator(PolicyOperator.getBySymbol(entity.getOperatore()));
		
		// alias for replacing in policy formula
		expression.setAlias(entity.getNInput());
		
		PolicyVariable rightVar = null;
		
		// LEFT op RIGHT
		if( Assorted.isNull(entity.getIdMisura())) {
			// left operand
			expression.setLeftVar(new PolicyVariable(entity.getIdInput(), 
					 VariableType.CATALOGO_DIFFORMITA));
			
			// right operand
			rightVar =  new PolicyVariable.PolicyVariableBuilder()
											  .rawValue(entity.getTargetRaffronto())
											  .type(VariableType.ALPHANUMERIC)
											  .build();
		} else {
			/**
			 * IDMISURA VALORIZZATO
			 */
			
			expression.setLeftVar(new PolicyVariable(entity.getIdMisura(), 
					 VariableType.MISURA));
			
			if(entity.getTargetNum() != null) {
				
				// VALORIZZATO TARGETNUM 
				rightVar =  new PolicyVariable.PolicyVariableBuilder()
						  .rawValue(String.valueOf(entity.getTargetNum()))
						  .type(VariableType.NUMERIC)
						  .build();
			} else if(Assorted.isNotEmpty(entity.getTargetRaffronto())) {
				rightVar =  PolicyOperator.PARI_DISPARI == expression.getOperator()
						? new PolicyVariable.PolicyVariableBuilder()
								.rawValue(String.valueOf(entity.getTargetRaffronto()))
								.type(VariableType.ALPHANUMERIC)
								.build()
						: new PolicyVariable.PolicyVariableBuilder()
								.name(String.valueOf(entity.getTargetRaffronto()))
								.type(VariableType.MODELLO)
								.build();
			} 
		}

		expression.setRightVar(rightVar);
		
		return expression;
	}
	
	/**
	 * i. se la colonna IDMISURA non è valorizzata (null) nella colonna IDINPUT sarà riportato l'id misura (id tabella X4CATMISURA). 
	 * Con tale chiave sarà ricercato nella mappa chiave valore relative alle misure associate all'asset il valore della misura. 
	 * Per questo tipo di condizione l'algoritmo prevede anche di recuperare la tabella di mapping dei possibili valori di gravità associati al fine poi di comparare 
	 * il livello di gravità riportate nel paragrafo 3.3.7. Se associati all'asset non sono presenti misure il valore della condizione identificata dal valore del 
	 * campo NInput della singola riga della tabella X4POLICYINPUT corrisponderà a false.
	 
	 * ii.	Se la colonna IDMISURA è valorizzata nella colonna nella colonna IDINPUT sarà riportato l'id della misura (id tabella x4CatMisure ) 
	 * in questo caso sarà necessario valutare il valore della variabile misura che popola appunto il campo IDMISURA. Il valore corrisponderà al X4CATMISURA. 
	 * Con questa chiave sarà ricercato nella mappa chiave/valore relativa alle misure associate all'asset il valore della variabile. 
	 * Se nel campo TARGETRAFFRONTONUM è valorizzato e TARGETRAFFRONTO è null il valore della variabile sarà comparata con un numerico, 
	 * se invece valorizzato il campo TARGETRAFFRONTO è valorizzato la chiave corrisponderà ad una chiave della tabella X4CATMODELLO. 
	 * Sulla della tabella X4CATMODELLOASSET e dei valori dell’asset sarà identificato il modello soglia da recuperare e il valore riportato nella colonna N1
	 * @param entity
	 * @return
	 */
	public static BinaryExpression variableExpression(X4PolicyInput entity) {
				
		// bean to retun
		BinaryExpression expression = new BinaryExpression();
		
		// data for audit and logging
		expression.setId(Long.valueOf(entity.getX4PolicyInputId()));
		// type of record
		expression.setType(PolicyInputType.fromCode(entity.getTipologiaInput()));
		
		// operator
		expression.setOperator(PolicyOperator.getBySymbol(entity.getOperatore()));
		
		// alias for replacing in policy formula
		expression.setAlias(entity.getNInput());
		
		PolicyVariable rightVar = null;
		
		// LEFT op RIGHT
		if( Assorted.isNull(entity.getIdMisura())) 
		{
			// left operand
			expression.setLeftVar(new PolicyVariable(entity.getIdInput(), VariableType.VARIABILE));
		} else {
		    expression.setLeftVar(new PolicyVariable(entity.getIdMisura(), VariableType.MISURA));
		}    
		
		if (Assorted.notNull(entity.getTargetNum()) && Assorted.isEmpty(entity.getTargetRaffronto())) 
        {
            // VALORIZZATO TARGETNUM 
            rightVar =  new PolicyVariable.PolicyVariableBuilder()
                                          .rawValue(String.valueOf(entity.getTargetNum()))
                                          .type(VariableType.NUMERIC)
                                          .build();
        } else if (Assorted.isNotEmpty(entity.getTargetRaffronto()) && Assorted.isNull(entity.getTargetNum())) {
            
            if (PolicyOperator.PARI_DISPARI == expression.getOperator())
            {
                rightVar = new PolicyVariable.PolicyVariableBuilder()
                                             .rawValue(String.valueOf(entity.getTargetRaffronto()))
                                             .type(VariableType.ALPHANUMERIC)
                                             .build();
                
            } else {
                rightVar = new PolicyVariable.PolicyVariableBuilder()
                                             .name(String.valueOf(entity.getTargetRaffronto()))
                                             .type(VariableType.MODELLO)
                                             .build();
            }
            
        } else if ((Assorted.isEmpty(entity.getTargetRaffronto()) && Assorted.isNull(entity.getTargetNum())) && (PolicyOperator.EXIST == expression.getOperator() || PolicyOperator.NOT_EXIST == expression.getOperator() || PolicyOperator.PARI == expression.getOperator() || PolicyOperator.DISPARI == expression.getOperator())) {
            
            rightVar = new PolicyVariable.PolicyVariableBuilder()
                                         .rawValue(entity.getTargetRaffronto())
                                         .type(VariableType.ALPHANUMERIC)
                                         .build();
        } else
            throw new IllegalArgumentException("VARIABLE configured incorrectly");
		
		expression.setRightVar(rightVar);
		return expression;
	}
	
	/**
	 * c.	Se TIPOLOGIAINPUT è valorizzato con HI nella colonna IDINPUT sarà riportato l'id Health index o tasso di guasto o tasso di difettosità 
	 * (id tabella AHMETHODOLOGY). Con tale chiave sarà ricercato nella mappa chiave/valore relative al HI associate all’asset il valore del HI.
	 * @param entity
	 * @return
	 */
	public static BinaryExpression healthIndexExpression(X4PolicyInput entity) {
		
		// type of record
		PolicyInputType inputType = PolicyInputType.fromCode(entity.getTipologiaInput());
		
		// bean to retun
		BinaryExpression expression = new BinaryExpression();
		
		// data for audit and logging
		expression.setId(Long.valueOf(entity.getX4PolicyInputId()));
		expression.setType(inputType);
		
		// operator
		expression.setOperator(PolicyOperator.getBySymbol(entity.getOperatore()));
		
		// alias for replacing in policy formula
		expression.setAlias(entity.getNInput());
		
		// left operand
		expression.setLeftVar(new PolicyVariable(entity.getIdInput(), 
				 VariableType.HEALTH_INDEX));
		
		// right operand
		PolicyVariableBuilder variableBuilder = new PolicyVariable.PolicyVariableBuilder();
		
		if(entity.getTargetNum() != null) {
			variableBuilder.type(VariableType.NUMERIC)
						   .rawValue(String.valueOf(entity.getTargetNum()));
			expression.setRightVar(variableBuilder.build());
		} else if(Assorted.isNotEmpty(entity.getTargetRaffronto())) {
			variableBuilder.type(VariableType.ALPHANUMERIC)
						   .rawValue(String.valueOf(entity.getTargetRaffronto()));
			expression.setRightVar(variableBuilder.build());
		}
		
		return expression;
	}
	
	/**
	 * e.	Se TIPOLOGIAINPUT è valorizzato con ATTIVITA nella colonna IDINPUT sarà riportato l'id attività (JPNUM tabella JOBPLAN). 
	 * Con tale chiave sarà ricercato nella mappa chiave valore relative alle attività associate agli ordini associate all'asset. 
	 * La condizione espressa in questi casi è legata alla presenza o meno di una attività associata all’asset quindi non sono previste trascodifiche
	 * tramite tabella di mappatura. 
	 */
	public static BinaryExpression attivitaExpression(X4PolicyInput entity) {
		
		// type of record
		PolicyInputType inputType = PolicyInputType.fromCode(entity.getTipologiaInput());
		
		// bean to retun
		BinaryExpression expression = new BinaryExpression();
		
		// data for audit and logging
		expression.setId(Long.valueOf(entity.getX4PolicyInputId()));
		expression.setType(inputType);
		
		// operator
		expression.setOperator(PolicyOperator.getBySymbol(entity.getOperatore()));
		
		// alias for replacing in policy formula
		expression.setAlias(entity.getNInput());
		
		// left operand
		expression.setLeftVar(new PolicyVariable(entity.getIdInput(), 
				 								 VariableType.ATTIVITA));
		
		// right operand not required, this policy will check if activity exists or not
		
		
		return expression;
	}
	
	/**
	 * d. Se TIPOLOGIAINPUT è valorizzato con INTERFERENZA nella colonna IDINPUT sarà riportato l'id interferenza (id tabella X4CATINTERF). 
	 * Con tale chiave sarà ricercato nella mappa chiave valore relative alle interferenze associate all'asset il valore corrispondente. 
	 * Per questo tipo di condizione l'algoritmo prevede anche di recuperare la tabella di mapping dei possibili valori di gravita' associati al fine 
	 * poi di comparare il livello di gravità per farlo saranno presenti in memoria le tabelle di catalogo dominio riportate nel paragrafo 3.3.7. 
	 * Se associati all'asset non sono presenti interferenze il valore della condizione identificata dal valore del campo NINPUT della singola riga della tabella 
	 * X4POLICYINPUT corrisponderà a false.
	 * @param entity
	 * @return
	 */
	public static BinaryExpression interferenzaExpression(X4PolicyInput entity) {
		
		BinaryExpression expression = new BinaryExpression();
		
		expression.setId(Long.valueOf(entity.getX4PolicyInputId()));
		expression.setOperator(PolicyOperator.getBySymbol(entity.getOperatore()));
		expression.setAlias(entity.getNInput());
		expression.setType(PolicyInputType.fromCode(entity.getTipologiaInput()));
		
		PolicyVariable leftVar = new PolicyVariable(entity.getIdInput(), 
				 									VariableType.CATALOGO_INTERFERENZA);
		
		PolicyVariable rightVar = new PolicyVariable.PolicyVariableBuilder()
				  .rawValue(entity.getTargetRaffronto())
				  .type(VariableType.ALPHANUMERIC)
				  .build();
		expression.setLeftVar(leftVar);
		expression.setRightVar(rightVar);

		return expression;
	}
	
	public static CombinationInput createCombination(X4PolicyInput entity) {
		String operator = null;
		String expression = null;
		if(Assorted.isNotEmpty(entity.getX4And())) {
			operator = "AND";
			expression = entity.getX4And();
		} else if(Assorted.isNotEmpty(entity.getX4Or())) {
			operator = "OR";
			expression = entity.getX4Or();
		} else if(Assorted.isNotEmpty(entity.getX4Equal())) {
			operator = "=";
			expression = entity.getX4Equal();
		} else if(Assorted.isNotEmpty(entity.getX4NotEqual())) {
			operator = "!=";
			expression = entity.getX4NotEqual();
		}
		return new CombinationInput(
				entity.getX4PolicyInputId(),
				operator, 
				PolicyTransformer.toIntArray(expression),
				entity.getLivInput());
	}
	
	/**
	 * 
	 * @param expr a string representing integer separeted by {@value Constants#COMBINATION_SEPARATOR} 
	 * {@see Constants#COMBINATION_SEPARATOR} 
	 * @return
	 */
	public static Long[] toIntArray(String expr) {
		Assert.hasLength(expr, "Expression is empty");
		
		String[] val = expr.trim().split(Constants.COMBINATION_SEPARATOR);
		
		Long[] result = new Long[val.length];
		
		for(int ii = 0; ii < val.length; ii++) {
			try {
				result[ii] = Long.parseLong(val[ii].trim());
			} catch (NumberFormatException e) {
				log.error("  - Error parsing expression formula in combination input. {}", expr);
			}
		}
		
		return result;
	}
	
	public static List<PolicyExpressionItem> toPolicyExpressions(Collection<ExpressionContext> list) {
		
		return  Assorted.isEmpty(list)
					? new ArrayList<>()
					: list.stream()
						  .map(ctx -> toPolicyExpressionItem(ctx))
				 	      .collect(Collectors.toList());
	}
	
	public static PolicyExpressionItem toPolicyExpressionItem(ExpressionContext ctx) {
		if(ctx == null) {
			return null;
		}
		
		var item = new PolicyExpressionItem();
		
		item.setExpressionFormula(ctx.getFormula());
		item.setExpressionResult(ctx.getResult());
		item.setFormulaId(String.valueOf(ctx.getExpression().getAlias()));
		item.setLeftVal(ctx.getLeftVal());
		item.setLeftVar(ctx.getExpression().getLeftVar());
		item.setOperator(ctx.getExpression().getOperator());
		item.setPolicyInputId(ctx.getExpression().getId());
		item.setRightVar(ctx.getExpression().getRightVar());
		item.setTargetVal(ctx.getTargetVal());
		item.setType(ctx.getExpression().getType());
		
		return item;
	}
	
	public static BinaryExpression tassoGuastoExpression(X4PolicyInput entity) {
		
		// type of record
		PolicyInputType inputType = PolicyInputType.fromCode(entity.getTipologiaInput());
		
		// bean to retun
		BinaryExpression expression = new BinaryExpression();
		
		// data for audit and logging
		expression.setId(Long.valueOf(entity.getX4PolicyInputId()));
		expression.setType(inputType);
		
		// operator
		expression.setOperator(PolicyOperator.getBySymbol(entity.getOperatore()));
		
		// alias for replacing in policy formula
		expression.setAlias(entity.getNInput());
		
		// left operand
		expression.setLeftVar(new PolicyVariable(entity.getIdInput(), 
				 VariableType.TASSO_GUASTO));
		
		// right operand
		PolicyVariableBuilder variableBuilder = new PolicyVariable.PolicyVariableBuilder();
		
		if(entity.getTargetNum() != null) {
			variableBuilder.type(VariableType.NUMERIC)
							.rawValue(String.valueOf(entity.getTargetNum()));
		} else if(Assorted.isNotEmpty(entity.getTargetRaffronto())) {
			variableBuilder.type(VariableType.ALPHANUMERIC)
			.rawValue(String.valueOf(entity.getTargetRaffronto()));
		}
		
		expression.setRightVar(variableBuilder.build());
		
		return expression;
	}
	
	public static BinaryExpression tassoAnomaliaExpression(X4PolicyInput entity) {
		
		// type of record
		PolicyInputType inputType = PolicyInputType.fromCode(entity.getTipologiaInput());
		
		// bean to retun
		BinaryExpression expression = new BinaryExpression();
		
		// data for audit and logging
		expression.setId(Long.valueOf(entity.getX4PolicyInputId()));
		expression.setType(inputType);
		
		// operator
		expression.setOperator(PolicyOperator.getBySymbol(entity.getOperatore()));
		
		// alias for replacing in policy formula
		expression.setAlias(entity.getNInput());
		
		// left operand
		expression.setLeftVar(new PolicyVariable(entity.getIdInput(), 
				 VariableType.TASSO_ANOMALIA));
		
		// right operand
		PolicyVariableBuilder variableBuilder = new PolicyVariable.PolicyVariableBuilder();
		
		if(entity.getTargetNum() != null) {
			variableBuilder.type(VariableType.NUMERIC)
							.rawValue(String.valueOf(entity.getTargetNum()));
		} else if(Assorted.isNotEmpty(entity.getTargetRaffronto())) {
			variableBuilder.type(VariableType.ALPHANUMERIC)
			.rawValue(String.valueOf(entity.getTargetRaffronto()));
		}
		
		expression.setRightVar(variableBuilder.build());
		
		return expression;
	}
}
