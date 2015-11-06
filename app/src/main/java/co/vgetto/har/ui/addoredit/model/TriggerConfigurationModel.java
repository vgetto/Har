package co.vgetto.har.ui.addoredit.model;

import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.db.entities.configurations.RecordingConfiguration;
import co.vgetto.har.db.entities.configurations.TriggerConfiguration;

/**
 * Created by Kovje on 30.10.2015..
 */
public class TriggerConfigurationModel {
  private int type;
  private String phoneNumber;

  public TriggerConfigurationModel() {

  }

  public TriggerConfigurationModel(int type, String phoneNumber) {
    this.type = type;
    this.phoneNumber = phoneNumber;
  }

  public static TriggerConfigurationModel fromTrigger(Trigger trigger) {
    TriggerConfiguration triggerConfiguration = trigger.triggerConfiguration();
    return new TriggerConfigurationModel(triggerConfiguration.type(), triggerConfiguration.number());
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
