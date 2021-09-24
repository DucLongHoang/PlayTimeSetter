package application;

public class BrowserThread extends Thread{
    private InitialController initCtrl;

    public BrowserThread(InitialController controller){
        this.initCtrl = controller;
    }

    public void run(){
        BrowserManipulator bm = new BrowserManipulator();
        PlayTimeCalculator calculator = null;
        try {
            calculator = new PlayTimeCalculator(bm.getAllMarks());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(calculator.listWeekMarks());
        initCtrl.enableNextScene(calculator);
    }

}
