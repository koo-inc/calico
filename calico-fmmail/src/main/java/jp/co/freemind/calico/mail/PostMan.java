package jp.co.freemind.calico.mail;

import java.util.List;

import jp.co.freemind.calico.core.media.Media;

/**
 * Created by kakusuke on 15/07/06.
 */
public interface PostMan {
  void accept(String envelopeJson);
  void accept(String envelopeJson, List<Media> media);
  void deliver(Mail mail);
}
