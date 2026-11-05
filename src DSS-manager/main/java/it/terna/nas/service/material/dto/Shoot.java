package it.terna.nas.service.material.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;

public class Shoot<T>
{
    private BrokerEntityInfo entity;
    private List<T>          items;

    public Shoot()
    {
        this.items      = new ArrayList<>();
    }
    
    public Shoot(BrokerEntityInfo entity)
    {
        this.entity     = Objects.requireNonNull(entity, "entity must not be null");
        this.items      = new ArrayList<>();
    }

    public Shoot(BrokerEntityInfo entity, T item)
    {
        this.entity     = Objects.requireNonNull(entity, "entity must not be null");
        this.items      = List.of(item);
    }
    
    public void setEntity(BrokerEntityInfo broker)
    {
        entity = broker;
    }
    
    public BrokerEntityInfo getEntity()
    {
        return entity;
    }

    public List<T> getItems()
    {
        return items;
    }

    @Override
    public String toString()
    {
        return "Shoot{ entity=" + entity + ", items.size=" + items.size() + '}';
    }
}
