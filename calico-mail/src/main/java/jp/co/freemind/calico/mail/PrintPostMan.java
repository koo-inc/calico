package jp.co.freemind.calico.mail;

import static java.util.stream.Collectors.toList;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Lists;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.core.media.MediaMeta;
import jp.co.freemind.calico.core.util.type.Tuple;
import jp.co.freemind.calico.core.util.type.Tuple2;

public class PrintPostMan implements PostMan {

  private List<Tuple2<Mail.Envelope, List<String>>> pairs = Lists.newArrayList();

  @Override
  public void accept(Mail.Envelope envelope, List<Media> mediaList) {
    List<String> mediaNames = mediaList.stream()
      .map(Media::getMeta)
      .map(MediaMeta::getName)
      .collect(toList());
    pairs.add(Tuple.of(envelope, mediaNames));
  }

  @Override
  public void deliver(Mail mail) {
    mail.visit(this);

    pairs.forEach(pair -> {
      Mail.Envelope envelope = pair.getValue1();
      envelope.getRecipients().forEach(recipient -> {
        System.out.println(String.format("##### [envelopeId: %s] [recipientId: %s]", envelope.getId(), recipient.getId()));
        System.out.println(String.format("# from: %s(%s)", envelope.getFromName(), envelope.getFromAddress()));
        System.out.println(String.format("# to: %s", recipient.getTo()));
        System.out.println(String.format("# replyTo: %s", envelope.getReplyTo()));
        System.out.println(String.format("# reservedAt: %s",
          envelope.getReservedAt() != null ? envelope.getReservedAt().format(DateTimeFormatter.ofPattern("uuuu/MM/dd hh:mm:ss")) : null));
        System.out.println(String.format("# subject: %s", envelope.getSubject()));
        System.out.println(String.format("# body: %s", envelope.getBody()));
        AtomicInteger integer = new AtomicInteger(1);
        pair.getValue2().forEach(attachment ->
          System.out.println(String.format("# attachment%d: %s", integer.getAndIncrement(), attachment)));
        System.out.println("#####\n");
      });
    });
    pairs = Lists.newArrayList();
  }
}
