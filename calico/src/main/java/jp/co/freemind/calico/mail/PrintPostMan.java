package jp.co.freemind.calico.mail;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import jp.co.freemind.calico.media.Media;
import jp.co.freemind.calico.media.MediaMeta;
import lombok.extern.log4j.Log4j2;

import static java.util.stream.Collectors.toList;

/**
 * Created by kakusuke on 15/07/06.
 */
@Log4j2
public class PrintPostMan implements PostMan {

  private List<Pair<String, List<String>>> pairs = Lists.newArrayList();

  @Override
  public void accept(String envelopeJson) {
    accept(envelopeJson, Collections.emptyList());
  }
  @Override
  public void accept(String envelopeJson, List<Media> mediaList) {
    List<String> mediaNames = mediaList.stream()
      .map(Media::getMeta)
      .map(MediaMeta::getName)
      .collect(toList());
    pairs.add(new ImmutablePair<>(envelopeJson, mediaNames));
  }

  @Override
  public void deliver(Mail mail) {
    mail.visit(this);

    pairs.forEach(pair -> {
      List<String> attachements = pair.getRight();
      Object[] args = Stream.concat(Stream.of(pair.getLeft()), attachements.stream())
        .collect(toList()).toArray();
      log.info("POST mail: {}" + Strings.repeat("\n\t{}", attachements.size()), args);
    });
    pairs = Lists.newArrayList();
  }
}
