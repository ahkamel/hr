package com.ahmed.hr.persistence.interceptor;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.ahmed.hr.business.listener.ApplicationStatusChangeListener;
import com.ahmed.hr.persistence.entities.Application;
import com.ahmed.hr.persistence.enums.ApplicationStatus;


//TODO: In Real application, I could need to make this a spring bean to be able,
//I left it like that for demo only
public class HrPersistenceInterceptor extends EmptyInterceptor {

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {

		if (entity instanceof Application) {
			for (int i = 0; i < propertyNames.length; i++) {
				if ("status".equals(propertyNames[i])) {
					if (currentState[i] != previousState[i]) {
						new ApplicationStatusChangeListener().onChange((Application) entity,
								(ApplicationStatus) currentState[i], (ApplicationStatus) previousState[i]);
					}
				}
			}
		}
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

		if (entity instanceof Application) {
			for (int i = 0; i < propertyNames.length; i++) {
				if ("status".equals(propertyNames[i])) {
					new ApplicationStatusChangeListener().onChange((Application) entity, (ApplicationStatus) state[i],
							null);
				}
			}
		}
		return super.onSave(entity, id, state, propertyNames, types);
	}
}
