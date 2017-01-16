package jp.co.freemind.calico.core.zone;

class ZoneRunnable implements Runnable {
  private final Zone zone;
  private final Processable processable;

  ZoneRunnable(Zone zone, Processable processable) {
    this.zone = zone;
    this.processable = processable;
  }

  @Override
  public void run() {
    zone.run(processable);
  }
}
