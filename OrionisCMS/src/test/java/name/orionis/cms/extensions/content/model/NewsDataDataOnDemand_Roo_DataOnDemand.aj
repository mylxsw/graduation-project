// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package name.orionis.cms.extensions.content.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import name.orionis.cms.extensions.content.model.NewsData;
import name.orionis.cms.extensions.content.model.NewsDataDataOnDemand;
import org.springframework.stereotype.Component;

privileged aspect NewsDataDataOnDemand_Roo_DataOnDemand {
    
    declare @type: NewsDataDataOnDemand: @Component;
    
    private Random NewsDataDataOnDemand.rnd = new SecureRandom();
    
    private List<NewsData> NewsDataDataOnDemand.data;
    
    public NewsData NewsDataDataOnDemand.getNewTransientNewsData(int index) {
        NewsData obj = new NewsData();
        setAllow_comment(obj, index);
        setContent(obj, index);
        setCopy_from(obj, index);
        setMax_char_per_page(obj, index);
        setPagination_type(obj, index);
        setTemplate(obj, index);
        return obj;
    }
    
    public void NewsDataDataOnDemand.setAllow_comment(NewsData obj, int index) {
        Boolean allow_comment = Boolean.TRUE;
        obj.setAllow_comment(allow_comment);
    }
    
    public void NewsDataDataOnDemand.setContent(NewsData obj, int index) {
        String content = "content_" + index;
        if (content.length() > 10000) {
            content = content.substring(0, 10000);
        }
        obj.setContent(content);
    }
    
    public void NewsDataDataOnDemand.setCopy_from(NewsData obj, int index) {
        String copy_from = "copy_from_" + index;
        if (copy_from.length() > 100) {
            copy_from = copy_from.substring(0, 100);
        }
        obj.setCopy_from(copy_from);
    }
    
    public void NewsDataDataOnDemand.setMax_char_per_page(NewsData obj, int index) {
        int max_char_per_page = index;
        obj.setMax_char_per_page(max_char_per_page);
    }
    
    public void NewsDataDataOnDemand.setPagination_type(NewsData obj, int index) {
        short pagination_type = new Integer(index).shortValue();
        obj.setPagination_type(pagination_type);
    }
    
    public void NewsDataDataOnDemand.setTemplate(NewsData obj, int index) {
        String template = "template_" + index;
        if (template.length() > 30) {
            template = template.substring(0, 30);
        }
        obj.setTemplate(template);
    }
    
    public NewsData NewsDataDataOnDemand.getSpecificNewsData(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        NewsData obj = data.get(index);
        Integer id = obj.getId();
        return NewsData.findNewsData(id);
    }
    
    public NewsData NewsDataDataOnDemand.getRandomNewsData() {
        init();
        NewsData obj = data.get(rnd.nextInt(data.size()));
        Integer id = obj.getId();
        return NewsData.findNewsData(id);
    }
    
    public boolean NewsDataDataOnDemand.modifyNewsData(NewsData obj) {
        return false;
    }
    
    public void NewsDataDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = NewsData.findNewsDataEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'NewsData' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<NewsData>();
        for (int i = 0; i < 10; i++) {
            NewsData obj = getNewTransientNewsData(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
