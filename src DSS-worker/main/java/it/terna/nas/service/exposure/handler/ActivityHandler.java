package it.terna.nas.service.exposure.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.AbstractHandler;
import it.terna.nas.service.dal.entity.eam.Activity;
import it.terna.nas.service.dal.entity.eam.AttributiAsset;
import it.terna.nas.service.dal.entity.eam.CondizioneAssociazione;
import it.terna.nas.service.dal.service.ActivityService;
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.dto.policy.Policy;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ActivityHandler extends AbstractHandler
{
	
	@Autowired
	private ActivityService activityService;
	
	public Activity getActivityByMacroActivity(AssetData assetData, Policy policy) {
		Assert.notNull(assetData, "assetData is null");
		Assert.notNull(policy, "policy is null");
		log.debug(" - Resolving activity by macro activity {}", policy.getMacroActivityId());
		
		CondizioneAssociazione associazione = null;
		List<CondizioneAssociazione> listAssociations = activityService.loadAssociations(policy.getMacroActivityId());
		
		if(Assorted.isNotEmpty(listAssociations)) 
		{
			conditionsLoop:
			for(CondizioneAssociazione condizioneAssociazione : listAssociations) 
			{
				// loop each condition
				for(AttributiAsset attr : assetData.getAttributi().values() ) 
				{
					// if condition is referring this atttribute
					if(Assorted.equals(attr.getAssetAttrId(), condizioneAssociazione.getTarget())) 
					{
						if(attr.getNumValue() != null) 
						{
							// check numValue if not null
							if (Objects.equals(convert(condizioneAssociazione.getValue()), attr.getNumValue()))
							{
								associazione = condizioneAssociazione;
							}
							
						} else {
							// or alnValue
							if (Objects.equals(condizioneAssociazione.getValue(), attr.getAlnValue()) || Objects.equals(condizioneAssociazione.getValue(), attr.getTableValue()))
								associazione = condizioneAssociazione;
						}
						// if an element match attrid and value
						if(associazione != null) {
							// break to outer loop.
							break conditionsLoop;
						}
					}
				}
			}
		}
		
		// Se nessuna condizione specifica è stata trovata, cerchiamo quella con target e value null
        if (associazione == null) 
        {
            for (CondizioneAssociazione condizioneAssociazione : listAssociations) 
            {
                if (condizioneAssociazione.getTarget() == null && condizioneAssociazione.getValue() == null) 
                {
                    associazione = condizioneAssociazione;
                    break;
                }
            }
        }

		Activity result = associazione != null ? toActivity(associazione) : null;
		
		log.info("getActivityByMacroActivity ==> {}", result);
		
		return result;
	}
	
	public Activity getActivityByJpnum(String jpnum) {
		Assert.hasText(jpnum, "assetData is null or empty");
		log.debug(" - Retrieving activity info using jpnum {}", jpnum);
		return activityService.getActivityByJpnum(jpnum);
	}
	
	private BigDecimal convert(String s) {
		try {
			return new BigDecimal(s);
		} catch (NumberFormatException e) {
			log.error("  - Error converting value in decimal format, value is '{}'", s);
		}
		return null;
	}
	
	private Activity toActivity(CondizioneAssociazione associazione) {
		if(associazione == null) {
			return null;
		}
		Activity activity = new Activity();
		activity.setClassificationId(associazione.getClassificationId());
		activity.setClassStructureId(associazione.getClassStructureId());
		activity.setDescription(associazione.getDescription());
		activity.setDuration(associazione.getDuration());
		activity.setIdMacroAttivita(associazione.getMacroAttivitaExt());
		activity.setJobPlanId(associazione.getAttivita());
		activity.setJpnum(associazione.getAttivitaExt());
		activity.setX4atridoneita(associazione.getX4atridoneita());
		activity.setX4Outsourcing(associazione.getX4Outsourcing());
		return activity;
	}

}
