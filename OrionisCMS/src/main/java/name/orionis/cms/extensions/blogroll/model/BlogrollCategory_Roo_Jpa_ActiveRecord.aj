// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package name.orionis.cms.extensions.blogroll.model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import name.orionis.cms.extensions.blogroll.model.BlogrollCategory;
import org.springframework.transaction.annotation.Transactional;

privileged aspect BlogrollCategory_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager BlogrollCategory.entityManager;
    
    public static final EntityManager BlogrollCategory.entityManager() {
        EntityManager em = new BlogrollCategory().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long BlogrollCategory.countBlogrollCategorys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BlogrollCategory o", Long.class).getSingleResult();
    }
    
    public static List<BlogrollCategory> BlogrollCategory.findAllBlogrollCategorys() {
        return entityManager().createQuery("SELECT o FROM BlogrollCategory o", BlogrollCategory.class).getResultList();
    }
    
    public static BlogrollCategory BlogrollCategory.findBlogrollCategory(Long id) {
        if (id == null) return null;
        return entityManager().find(BlogrollCategory.class, id);
    }
    
    public static List<BlogrollCategory> BlogrollCategory.findBlogrollCategoryEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BlogrollCategory o", BlogrollCategory.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void BlogrollCategory.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void BlogrollCategory.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            BlogrollCategory attached = BlogrollCategory.findBlogrollCategory(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void BlogrollCategory.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void BlogrollCategory.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public BlogrollCategory BlogrollCategory.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BlogrollCategory merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
