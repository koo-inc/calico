package jp.co.freemind.calico.mail;

import java.util.Collections;
import java.util.List;

import jp.co.freemind.calico.core.media.Media;

public interface PostMan {
  default void accept(Mail.Envelope envelope) {
    accept(envelope, Collections.emptyList());
  }
  void accept(Mail.Envelope envelope, List<Media> media);
  void deliver(Mail mail);
}
