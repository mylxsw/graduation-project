package name.orionis.cms.extensions.advertisement.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import name.orionis.cms.core.base.Form;
import name.orionis.cms.extensions.advertisement.model.Advertisement;
/**
 * Advertisement Form
 * @author orionis
 * @2013-5-22
 * Site : http://blog.orionis.name
 *
 */
public class AdvertisementForm extends Form<Advertisement> {

	@NotNull
    @Size(min=1, max = 50, message="标题不能为空!")
    private String title;
    

    @Size(min = 0, max = 500, message="内容不能多于500个字符!")
    private String content;
	
	@Override
	public Advertisement toEntity() {
		Advertisement adv = new Advertisement();
		adv.setTitle(title);
		adv.setContent(content);
		return adv;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
