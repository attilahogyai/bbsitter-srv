package org.mfr.data;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class AbstractDao<T> {
	private static final Log log = LogFactory.getLog(AbstractDao.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	public void persist(T transientInstance) {
		persistG(transientInstance);
	}
	public EntityManager getEntityManager(){
		return entityManager;
	}
	public void persistG(Object transientInstance) {
		log.debug("persisting "+transientInstance.getClass());
		try {
			try{
				PropertyUtils.setSimpleProperty(transientInstance, "modifyDt",new Date());
			}catch(Exception e){
			}
			entityManager.persist(transientInstance);
			entityManager.flush();
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	public void remove(T persistentInstance) {
		removeG(persistentInstance);
	}
	public void removeG(Object persistentInstance) {
		log.debug("removing instance");
		try {
			persistentInstance=entityManager.merge(persistentInstance);
			entityManager.remove(persistentInstance);
			entityManager.flush();
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}
	
	public Object merge(T detachedInstance) {
		return mergeG(detachedInstance); 
	}
	public Object mergeG(Object detachedInstance) {
		log.debug("merging "+detachedInstance.getClass());
		try {
			try{
				PropertyUtils.setSimpleProperty(detachedInstance, "modifyDt",new Date());
			}catch(Exception e){
			}
			Object result = entityManager.merge(detachedInstance);
			entityManager.flush();
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	@SuppressWarnings("unchecked")
	public List<T>findAll(Class<T> clazz){
		log.debug("findAll "+clazz.getName());
		Query result = entityManager.createQuery("from "+clazz.getName());
		return result.getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<T>findAll(Class<T> clazz,Integer[] ids){
		log.debug("findAll "+clazz.getName());
		Query result = entityManager.createQuery("from "+clazz.getName()+" where id in ("+org.apache.commons.lang.StringUtils.join(ids, ',')+")");
		return result.getResultList();
	}
	@SuppressWarnings("rawtypes")
	public List findAllG(Class clazz){
		log.debug("findAll "+clazz.getName());
		Query result = entityManager.createQuery("from "+clazz.getName());
		return result.getResultList();
	}
	@SuppressWarnings("rawtypes")
	public void refresh(Object entity){
		log.debug("refresh "+entity.getClass().getSimpleName());
		entityManager.refresh(entity);
	}
	public T findById(Integer id,Class<T> clazz){
		return entityManager.find(clazz, id);
	}
	@SuppressWarnings("rawtypes")
	public Object findByIdG(Integer id,Class clazz){
		return entityManager.find(clazz, id);
	}
	public void evict(Object o){
		Session session=(Session)entityManager.getDelegate();
		session.evict(o);
	}
}
