package it.terna.nas.service.material.utility;

import java.util.ArrayList;
import java.util.List;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.nas.service.dal.entity.eam.WorkOrder;
import it.terna.nas.service.dal.entity.eam.X4DssOrders;
import it.terna.nas.service.material.dto.worker.Order;
import it.terna.nas.service.material.utility.Catalog.VeroOrFalso;

public class OrderUtility
{
    public static List<Order> convertFromWorkOrder(List<WorkOrder> listOrderDss)
    {
        List<Order> result = new ArrayList<>();
        
        if (Assorted.isNotEmpty(listOrderDss))
        {
            for (WorkOrder obj: listOrderDss)
                result.add(convertFromWorkOrder(obj));    
        }
        
        return result;
    }
    
    
    public static Order convertFromWorkOrder(WorkOrder wo)
    {
        Order order = null;
        
        if (Assorted.notNull(wo))
        {
            order = new Order();
            order.setAssetnum(wo.getAssetNum());
            order.setStato(wo.getStatus());
            order.setJpnum(wo.getJpnum());
            order.setX4PolicyId(wo.getX4Policyid());
            order.setIdPolicy(wo.getX4Policy());
            order.setParent(wo.getParent());
            order.setDataModello(wo.getX4DataModello());
            order.setDataPianificataInizio(wo.getTargStartDate());
            order.setDataPianificataFine(wo.getTargCompDate());
            order.setDataMaxPosticipo(wo.getFnlConstraint());
            order.setDataNonPrimaDi(wo.getSneConstraint());
            order.setTronconum(wo.getX4TroncoNum());
            order.setX4Trainante(wo.getX4Trainante());
            order.setX4Note(wo.getX4Note());
            order.setX4OrdinePrevalente(wo.getX4OrdinePrevalente()); 
            order.setToCreate(false);
            order.setWonum(wo.getWonum());
            order.setSapIdAsset(wo.getX4SapIdAsset());
            order.setX4aggregati(wo.getX4Aggregati());
            order.setX4outsourcing(wo.getX4outsourcing());
        }
        
        return order;
    }
    
    
    public static List<Order> convertFromX4DssOrders(List<X4DssOrders> listOrderDss)
    {
        List<Order> result = new ArrayList<>();
        
        if (Assorted.isNotEmpty(listOrderDss))
        {
            for (X4DssOrders obj: listOrderDss)
                result.add(convertFromX4DssOrders(obj));    
        }
        
        return result;
    }
    
    
    public static Order convertFromX4DssOrders(X4DssOrders orderDss)
    {
        Order order = null;
        
        if (Assorted.notNull(orderDss))
        {
            order = new Order();
            order.setAssetnum(orderDss.getAssetNum());
            order.setStato(orderDss.getStatus());
            order.setJpnum(orderDss.getJpNum());
            order.setX4PolicyId(orderDss.getX4PolicyId());
            order.setIdPolicy(orderDss.getX4Policy());
            order.setParent(orderDss.getWoParent());
            order.setDataModello(orderDss.getDataModello());
            order.setDataPianificataInizio(orderDss.getDataPianifInizio());
            order.setDataPianificataFine(orderDss.getDataPianifFine());
            order.setDataMaxPosticipo(orderDss.getDataMaxPosticipo());
            order.setDataNonPrimaDi(orderDss.getDataNonPrimaDi());
            order.setTronconum(orderDss.getTroncoNum());
            order.setX4Trainante(orderDss.getX4Trainante());
            order.setX4Note(orderDss.getX4Note());
            order.setX4OrdinePrevalente(orderDss.getX4WoPrevalente()); 
            order.setToCreate(VeroOrFalso.SI.getCode().equals(orderDss.getToCreate()));
            order.setWonum(orderDss.getWonum());
            order.setSapIdAsset(orderDss.getSapIdAsset());
            order.setX4outsourcing(orderDss.getX4outsourcing());
        }
        
        return order;
    }    
}
