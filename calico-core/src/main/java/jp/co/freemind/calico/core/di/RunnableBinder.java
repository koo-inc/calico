package jp.co.freemind.calico.core.di;

class RunnableBinder implements Runnable {
  private final InjectorRef injectorRef;
  private final Processable processable;

  RunnableBinder(InjectorRef injectorRef, Processable processable) {
    this.injectorRef = injectorRef;
    this.processable = processable;
  }

  @Override
  public void run() {
    injectorRef.run(processable);
  }
}
