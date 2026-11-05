package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNull;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.lettuce.core.RedisException;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.Activity;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.HealthIndexAsset;
import it.terna.nas.service.dal.entity.eam.Order;
import it.terna.nas.service.dal.entity.eam.OrderActions;
import it.terna.nas.service.dal.entity.eam.X4DssAggregate;
import it.terna.nas.service.dal.entity.eam.X4PolicyAggregateEleg;
import it.terna.nas.service.dal.repository.AssetRepository;
import it.terna.nas.service.dal.repository.WorkOrderRepository;
import it.terna.nas.service.dal.repository.X4DssAggregateRepository;
import it.terna.nas.service.dal.repository.X4PolicyAggregateElegRepository;
import it.terna.nas.service.dal.service.AssetDataService;
import it.terna.nas.service.dal.service.WorkOrderQueryService;
import it.terna.nas.service.dal.service.X4CatModelloAssetService;
import it.terna.nas.service.dal.service.X4PolicyService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.PolicyRequestEvent;
import it.terna.nas.service.exposure.events.WorkerFeedbackRequestEvent;
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.dto.EsitoElab;
import it.terna.nas.service.material.dto.policy.Policy;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.EleggibilitaType;
import it.terna.nas.service.material.utility.Catalog.PolicyType;
import it.terna.nas.service.material.utility.Catalog.StatusX4Esito;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AggregateCondFuneTelHandler extends AbstractDssHandler
{
    private static final String      HI_DEGRADO_CONDUTTORE      = "1027";
    private static final String      HI_DEGRADO_FUNE_DI_GUARDIA = "1014";
    private static final String      HI_VETUSTA_CONDUTTORE      = "1031";
    private static final String      HI_VETUSTA_FUNE_DI_GUARDIA = "1013";
    private static final String      HI_VETUSTA_STRUTTURA       = "1044";
    private static final String      HI_TASSO_GUASTO_STRUTTURA  = "1057";
    private static final String      S1COND                     = "S1COND";
    private static final String      S1FDG                      = "S1FDG";
    
    @Autowired
    private ActivityHandler activityHandler;
    
    @Autowired
    private AssetDataService assetDataService;

    @Autowired
    private AssetRepository  assetRepository;

    @Autowired
    private OrderHandler orderHandler;
    
    @Autowired
    private X4PolicyService policyService;
    
    @Autowired
    private PrevalentOrderHandler prevalentOrderHandler;
    
    @Autowired
    private WorkOrderQueryService workOrderQueryService;

    @Autowired
    private WorkOrderRepository workOrderRepository;
    
    @Autowired
    private X4CatModelloAssetService x4CatModelloAssetService;
    
    @Autowired
    private X4DssAggregateRepository x4DssAggregateRepository;
    
    @Autowired
    private X4PolicyAggregateElegRepository x4PolicyAggregateElegRepository;
    
    private List<X4DssAggregate> calculateKeleg3Level(Policy policy, AssetData assetData, String hiConstDegrado, String hiConstVetusta, String s1Const) 
            throws EAMException
    {
        List<X4DssAggregate> result = new ArrayList<>();
        X4DssAggregate data         = new X4DssAggregate();
        List<Asset> listAssetChild  = null;
        Asset asset                 = assetData.getAsset();
        
        if (Constants.CLASS_CONDUTTORI.equals(asset.getClassificationId()))
            listAssetChild = assetRepository.findAssetToConduttori(asset.getAssetNum());
        else
            listAssetChild = assetRepository.findAssetToFuneDiGuardia(asset.getAssetNum());
        
        List<Double> keleg4Level   = findKeleg4Level(policy, asset, listAssetChild, hiConstDegrado, hiConstVetusta, s1Const);
        Set<String> assetLivel2    = new HashSet<>();
        data.setAssetNum(asset.getAssetNum());
        data.setAssetNumLv2(asset.getParent()); //Campata
        if (isNotEmpty(keleg4Level))
        {
            data.setKeligibilita(keleg4Level.stream().max(Double::compare).get());
            for(Asset child: listAssetChild) 
            {
                AssetData assetChild  = assetDataService.getAssetDataByAssetNum(child.getAssetNum(), PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()));
                List<String> tesatura = assetDataService.findAssetForTesatura(assetChild, assetData);
                if (isNotEmpty(tesatura))
                {
                    for (String assetJTesatura: tesatura)
                    {
                        AssetData assetJ = assetDataService.getAssetDataByAssetNum(assetJTesatura, PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()));
                        if (notNull(assetJ) && isNotEmpty(assetJ.getAsset().getAncestor()))
                            assetLivel2.add(assetJ.getAsset().getAncestor());
                    }
                }
            }
            
            if (isNotEmpty(assetLivel2))
            {
                List<String> assetLivel2List = new ArrayList<String>(assetLivel2);
                Collections.sort(assetLivel2List);
                data.setTratta(String.join(Constants.SEP5, assetLivel2List));
            }
            
        } else {
            return result;
        }
        
        if (Constants.CLASS_CONDUTTORI.equals(asset.getClassificationId()))
        {    
            data.setLinea(asset.getX3RootAssetNum());
            result.add(data);
        } else {
            List<String> lineeList = assetRepository.findLineeForAssetLocRelation(asset.getParent());
            
            if (isNotEmpty(lineeList))
            {
                data.setLinea(lineeList.get(0));
                result.add(data);
                
                if (lineeList.size() > 1)
                {
                    X4DssAggregate data2 = new X4DssAggregate();
                    data2.setAssetNum(data.getAssetNum());
                    data2.setAssetNumLv2(data.getAssetNumLv2());
                    data2.setTratta(data.getTratta());
                    data2.setLinea(lineeList.get(1));
                    data2.setKeligibilita(data.getKeligibilita());
                    result.add(data2);
                }
            }
        }
        
        return result;
    }
    
    private List<X4DssAggregate> calculateKelegStruttura(Policy policy, AssetData assetData, double s1Struttura) 
            throws EAMException
    {
        List<X4DssAggregate> result     = new ArrayList<>();
        Asset asset                     = assetData.getAsset();
        List<Asset> listAssetChild      = assetRepository.findAssetToStruttura(asset.getAssetNum());
        List<Double> listKelegStruttura = findKelegStruttura(policy, listAssetChild, s1Struttura);
        if (isNotEmpty(listKelegStruttura))
        {
            Double kelegStruttura   = listKelegStruttura.stream().max(Double::compare).get();
            List<String> lineeList = assetRepository.findLineeForAssetLocRelation(asset.getParent());
            if (isNotEmpty(lineeList))
            {
                for (String linea: lineeList)
                {
                    X4DssAggregate data = new X4DssAggregate();
                    data.setAssetNum(asset.getAssetNum());
                    data.setAssetNumLv2(asset.getParent());
                    data.setLinea(linea);
                    data.setKeligibilita(kelegStruttura);
                    result.add(data);
                }
            }
        }
        
        return result;
    }

    
    private List<Double> findKeleg4Level(Policy policy, Asset asset, List<Asset> listAssetChild, String hiConstDegrado, String hiConstVetusta, String s1Const) 
            throws EAMException
    {
        log.debug("call findKeleg4Level({})", asset.getAssetNum());
        List<Double> result        = new ArrayList<>();
        if (isNotEmpty(listAssetChild))
        {
            Double s1Cond = null;
            
            Map<String, Double> catValue = x4CatModelloAssetService.getByIdModello(s1Const);
            if (isEmpty(catValue))
            {
                log.warn("No data in X4CATMODELLOASSET for idModello {}", s1Const);
                return result;
            }
            
            final String assetSpecName = Constants.CLASS_CONDUTTORI.equals(asset.getClassificationId()) ? Constants.ASSET_CONDUTTORE : Constants.ASSET_FUNE_GUARDIA;
            for (Asset assetObj: listAssetChild)
            {
                //Determinazione S1Cond
                AssetData assetData  = assetDataService.getAssetDataByAssetNum(assetObj.getAssetNum(), PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()));
                
                String assetValue    = assetData.getAttributi()
                                                .entrySet()
                                                .stream()
                                                .filter(entry -> assetSpecName.equals(entry.getKey()))
                                                .findFirst()
                                                .map(entry -> entry.getValue().getAlnValue())
                                                .orElse(null);

                if (isNotEmpty(assetValue))
                    s1Cond = catValue.get(assetValue);
                
                if (isNull(s1Cond))
                {
                    log.debug("Impossible to find s1 for asset {}", assetObj.getAssetNum());
                    continue;
                }
                
                Map<String, HealthIndexAsset> hiMap = assetData.getHi();
                
                if (isEmpty(hiMap))
                {
                    log.debug("No HI for asset {}", assetObj.getAssetNum());
                    continue;
                }
                
                
                //Determinazione HiDegrado ed HiVetusta
                Double hiDegrado = null;
                Double hiVetusta = null;
                
                HealthIndexAsset healthIndexAsset1 = hiMap.get(hiConstDegrado);
                if (notNull(healthIndexAsset1))
                    hiDegrado = healthIndexAsset1.getValue();

                HealthIndexAsset healthIndexAsset2 = hiMap.get(hiConstVetusta);
                if (notNull(healthIndexAsset2))
                    hiVetusta = healthIndexAsset2.getValue();
                
                log.debug("hiDegrado: {}, hiVetusta: {}", hiDegrado, hiVetusta);
                
                //Calcolo Keleg_cond o Keleg_fdg  
                Double kEleg = null;
                if (notNull(hiDegrado) && notNull(hiVetusta) && s1Cond != 0)
                {
                    if ((hiDegrado + hiVetusta) >= s1Cond)
                        kEleg = 1D;
                    else {
                        List<String> listWonum = workOrderRepository.findWonumByStatus(assetObj.getAssetNum());
                        if (isNotEmpty(listWonum))
                            kEleg = 1D;
                        else
                            kEleg = (hiDegrado + hiVetusta) / s1Cond; 
                    }
                        
                } else {
                    List<String> listWonum = workOrderRepository.findWonumByStatus(assetObj.getAssetNum());
                    if (isNotEmpty(listWonum))
                        kEleg = 1D;
                }
                
                log.debug("kEleg: {}", kEleg);
                
                if (notNull(kEleg))
                    result.add(kEleg);
            }
        }
        
        
        log.info("end findKeleg4Level({}) ==> {}", asset.getAssetNum(), result);
        
        return result;
    }

    
    private List<Double> findKelegStruttura(Policy policy, List<Asset> strutture, double s1Struttura) 
            throws EAMException
    {
        if (log.isDebugEnabled() && isNotEmpty(strutture))
        {
            String stringsAsset = strutture.stream().map(Asset::getAssetNum).collect(Collectors.joining(Constants.SEP2));
            log.debug("call findKelegStruttura({}, {})", stringsAsset, s1Struttura);
        }

        List<Double> result = new ArrayList<>();
        if (isNotEmpty(strutture))
        {    
            for(Asset asset: strutture)
            {
                AssetData assetData  = assetDataService.getAssetDataByAssetNum(asset.getAssetNum(), PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()));
                Map<String, HealthIndexAsset> hiMap     = assetData.getHi();
                if (isEmpty(hiMap))
                {
                    log.debug("No HI for asset {}", asset.getAssetNum());
                    continue;
                }
                
                Double hiVetusta = null;
                HealthIndexAsset healthIndexAsset1 = hiMap.get(HI_VETUSTA_STRUTTURA);
                if (notNull(healthIndexAsset1))
                    hiVetusta = healthIndexAsset1.getValue();
                
                Double hiTdg = null;
                HealthIndexAsset healthIndexTdg2   = hiMap.get(HI_TASSO_GUASTO_STRUTTURA);
                if (notNull(healthIndexTdg2))
                    hiTdg = healthIndexTdg2.getValue();
                
                log.debug("hiTdg: {} hiVetusta: {}", hiTdg, hiVetusta);
                
                Double kEleg = null;
                if (notNull(hiTdg) && notNull(hiVetusta) && s1Struttura != 0)
                {
                    if ((hiTdg + hiVetusta) >= s1Struttura)
                        kEleg = 1D;
                    else {
                        List<String> listWonum = workOrderRepository.findWonumByStatus(asset.getAssetNum());
                        if (isNotEmpty(listWonum))
                            kEleg = 1D;
                        else
                            kEleg = (hiTdg + hiVetusta) / s1Struttura; 
                    }
                        
                } else {
                    List<String> listWonum = workOrderRepository.findWonumByStatus(asset.getAssetNum());
                    if (isNotEmpty(listWonum))
                        kEleg = 1D;
                }
                
                log.info("end findKelegStruttura({}) ==> {}", asset.getAssetNum(), kEleg);
                
                if (notNull(kEleg))
                    result.add(kEleg);
            }
        }   
        
        return result;
    }


    public boolean manageAggregate(PolicyRequestEvent event, Policy policy, AssetData assetData) 
            throws EAMException
    {
        Asset asset = assetData.getAsset();
        if (Constants.CLASS_CONDUTTORI.equals(asset.getClassificationId()))
            return manageConduttori(event, policy, assetData);
        else if (Constants.CLASS_FUNI_GUARDIA.equals(asset.getClassificationId())) 
            return manageFuniDiGuardia(event, policy, assetData);
        else if (Constants.CLASS_TELAIO.equals(asset.getClassificationId()))
            return manageTelaio(event, policy, assetData);
        
        return false;
    }
    
    
    private boolean manageConduttori(PolicyRequestEvent event, Policy policy, AssetData assetData) 
            throws EAMException
    {
        boolean esito = true;
        List<X4DssAggregate> dataList = calculateKeleg3Level(policy, assetData, HI_DEGRADO_CONDUTTORE, HI_VETUSTA_CONDUTTORE, S1COND);
        for (X4DssAggregate data: dataList)
        {    
            if (isNotEmpty(data.getTratta()) && isNotEmpty(data.getLinea()) && isNotEmpty(data.getAssetNumLv2()))
            {
                data.setDssPcCorrelationId(event.getCorrelationId());
                data.setIdDssWorked(Long.parseLong(event.getDssWorkerId()));
            } else {
                esito = false;
                break;
            }
        }

        if (esito && isNotEmpty(dataList))
            x4DssAggregateRepository.saveAll(dataList);
        else
            esito = false;
        
        return esito;
    }
    
    private boolean manageFuniDiGuardia(PolicyRequestEvent event, Policy policy, AssetData assetData) 
            throws EAMException
    {
        boolean esito = true;
        List<X4DssAggregate> dataList = calculateKeleg3Level(policy, assetData, HI_DEGRADO_FUNE_DI_GUARDIA, HI_VETUSTA_FUNE_DI_GUARDIA, S1FDG);
        for (X4DssAggregate data: dataList)
        {    
            if (isNotEmpty(data.getTratta()) && isNotEmpty(data.getLinea()) && isNotEmpty(data.getAssetNumLv2()))
            {
                data.setDssPcCorrelationId(event.getCorrelationId());
                data.setIdDssWorked(Long.parseLong(event.getDssWorkerId()));
            } else {
                esito = false;
                break;
            }
        }

        if (esito && isNotEmpty(dataList))
            x4DssAggregateRepository.saveAll(dataList);
        else
            esito = false;
        
        return esito;
    }


    private boolean manageTelaio(PolicyRequestEvent event, Policy policy, AssetData assetData) 
            throws EAMException
    {
        boolean esito = true;
        X4PolicyAggregateEleg s1StrutturaEleg = x4PolicyAggregateElegRepository.findByParameter(EleggibilitaType.S1_STRUTTURA.getCode());
        final double s1Struttura              = s1StrutturaEleg.getValue();
        List<X4DssAggregate> dataList         = calculateKelegStruttura(policy, assetData, s1Struttura);
        for (X4DssAggregate data: dataList)
        {    
            if (isNotEmpty(data.getLinea()) && isNotEmpty(data.getAssetNumLv2()))
            {
                data.setDssPcCorrelationId(event.getCorrelationId());
                data.setIdDssWorked(Long.parseLong(event.getDssWorkerId()));
            } else {
                esito = false;
                break;
            }
        }

        if (esito && isNotEmpty(dataList))
            x4DssAggregateRepository.saveAll(dataList);
        else
            esito = false;
        
        return esito;
    }


    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public void saveOdL(PolicyRequestEvent event)
        throws EAMRetryException
    {
        EsitoElab esito       = new EsitoElab();
        esito.setEsito(EAMErrorMapping.EAM_0000);

        try {
            Policy policy               = policyService.loadPolicy(event.getPolicy());
            List<Order> listOrder       = new ArrayList<>();
            List<Asset> listAssetChild  = null;
            
            if (Constants.AGGREGATE_COND_SAVE.equals(event.getEvent()))
                listAssetChild = assetRepository.findAssetToConduttoriFromLinea(event.getAsset());
            else if (Constants.AGGREGATE_FUNE_SAVE.equals(event.getEvent()))
                listAssetChild = assetRepository.findAssetToFuneDiGuardiaFromLinea(event.getAsset());
            else if (Constants.AGGREGATE_STRUTTURA_SAVE.equals(event.getEvent()))
                listAssetChild = assetRepository.findAssetToStrutturaFromLinea(event.getAsset());
            
            if (isNotEmpty(listAssetChild))
            {
                for(Asset assetJ: listAssetChild)
                {
                    AssetData assetData = assetDataService.getAssetDataByAssetNum(assetJ.getAssetNum(), PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()));
                    Activity activity   = activityHandler.getActivityByMacroActivity(assetData, policy);
                    Order order         = orderHandler.createOrder(assetData, policy, activity);
                    if (Assorted.notNull(order))
                        listOrder.add(order);
                }
                
                if (isNotEmpty(listOrder))
                { 
                    Order orderFirst        = listOrder.get(0);
                    AssetData assetLinea    = assetDataService.getAssetDataByAssetNum(event.getAsset(), PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()));
                    
                    // Controllo se ordine già è presente
                    String existingWonum = workOrderQueryService.getWonumByPolicyAssetAndJpnum(
                            policy.getId(),
                            orderFirst.getJpnum(),
                            assetLinea.getAsset().getAssetNum()
                    );
                                     
                    if (isEmpty(existingWonum))
                    {
                        Order rootOrder = new Order(orderFirst);
                        rootOrder.setAssetnum(event.getAsset());
                        rootOrder.setParent(null);
                        rootOrder.setWonum(Constants.PREFIX_DSS_WONUM + workOrderQueryService.getSequenceForAggregate().get(0).getNextval());
                        rootOrder.setX4aggregati(1);
                        listOrder.stream().forEach(x -> x.setParent(rootOrder.getWonum()));
                        listOrder.add(rootOrder);
                    } else {
                        listOrder.stream().forEach(x -> x.setParent(existingWonum));
                    }
                    
                    OrderActions orderActions   = prevalentOrderHandler.checkPrevalentOrder(listOrder, policy);
                    checkStatusOrderActions(orderActions);
                    esito.setListPairAssetWonum(orderHandler.sendOrders(orderActions, event.getAsset(), policy, esito));
                } else {
                    esito.setEsito(EAMErrorMapping.EAM_0009);
                }
                
            } else {
                esito.setEsito(EAMErrorMapping.EAM_0009);
            }
            
        } catch(RedisException | DataLayerException | QueryTimeoutException l) {
            esito.setEsito(EAMErrorMapping.EAM_9999);
            esito.setDescription(l.getMessage());
            log.error(l.getMessage(), l);
            throw new EAMRetryException(esito.getEsito().getEamCode(), esito.getDescription());
        } catch(EAMException q) {
            esito.setEsito(EAMErrorMapping.findCausaleEsito(q.getErrorCode()));
            esito.setDescription(q.getMessage());
            log.error(q.getMessage(), q);
            if (q instanceof EAMRetryException)
                throw (EAMRetryException) q;
        } catch(Exception e) {
            esito.setEsito(EAMErrorMapping.EAM_9999);
            esito.setDescription(e.getMessage());
            log.error(" - Error while evaluating policy", e);
        } finally {
            updateStatus(event.getDssWorkerId(), event.getCorrelationId(), esito);
        }
    }
    
    
    public void sendWorkerFeedbackEvent(PolicyRequestEvent requestEvent, boolean esito)
    {
        log.debug("call sendWorkerFeedbackEvent({})", requestEvent.getCorrelationId());
        
        WorkerFeedbackRequestEvent event = new WorkerFeedbackRequestEvent();
        event.setAsset(requestEvent.getAsset());
        event.setCorrelationId(requestEvent.getCorrelationId());
        event.setEvent(requestEvent.getEvent());
        event.setPolicy(requestEvent.getPolicy());
        event.setNumberMessagePolicy(requestEvent.getNumberMessagePolicy());
        event.setTipology(requestEvent.getTipology());
        event.setType(requestEvent.getType());
        
        if (esito) 
        {
            event.setEsito(StatusX4Esito.OK.name());
            log.debug("Orders set in the event. Esito: {}", StatusX4Esito.OK.name());
        } else {
            event.setEsito(StatusX4Esito.KO.name());
            log.debug("No orders found. Esito set to {}", StatusX4Esito.KO.name());
        }
    
        BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(Constants.END_POINT_HANDLE_WORKER_FEEDBACK);
        kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
    }
}
