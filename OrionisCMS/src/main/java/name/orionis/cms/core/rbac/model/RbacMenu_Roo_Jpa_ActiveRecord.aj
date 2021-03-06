// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package name.orionis.cms.core.rbac.model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import name.orionis.cms.core.rbac.model.RbacMenu;
import org.springframework.transaction.annotation.Transactional;

privileged aspect RbacMenu_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager RbacMenu.entityManager;
    
    public static final EntityManager RbacMenu.entityManager() {
        EntityManager em = new RbacMenu().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long RbacMenu.countRbacMenus() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RbacMenu o", Long.class).getSingleResult();
    }
    
    public static List<RbacMenu> RbacMenu.findAllRbacMenus() {
        return entityManager().createQuery("SELECT o FROM RbacMenu o", RbacMenu.class).getResultList();
    }
    
    public static RbacMenu RbacMenu.findRbacMenu(Long id) {
        if (id == null) return null;
        return entityManager().find(RbacMenu.class, id);
    }
    
    public static List<RbacMenu> RbacMenu.findRbacMenuEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RbacMenu o", RbacMenu.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void RbacMenu.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void RbacMenu.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            RbacMenu attached = RbacMenu.findRbacMenu(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void RbacMenu.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void RbacMenu.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public RbacMenu RbacMenu.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        RbacMenu merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
