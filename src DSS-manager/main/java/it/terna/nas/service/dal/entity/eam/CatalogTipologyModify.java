package it.terna.nas.service.dal.entity.eam;

import it.terna.nas.service.material.utility.Catalog.CatalogTipologyType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter @Setter
public class CatalogTipologyModify {
	
	private CatalogTipologyType type;
	
	private String key;

}

