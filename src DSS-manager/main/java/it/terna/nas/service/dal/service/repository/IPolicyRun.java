package it.terna.nas.service.dal.service.repository;

import java.util.Date;

public interface IPolicyRun
{
	 public Long getRunnerId();
	 
	 public String getCorrelationId();
	 
	 public Date getElaborationDate();
	 
	 public Long getX4PolicyId();
}
