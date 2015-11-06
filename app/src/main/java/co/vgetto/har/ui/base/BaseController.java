package co.vgetto.har.ui.base;

/**
 * Created by Kovje on 19.10.2015..
 */
public interface BaseController {
  void init(BaseModel savedModel);
  BaseModel getModel();
}
