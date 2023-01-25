import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends JPanel {
    private final Settings settings;
    public SettingsPanel(Main main,Settings startSettings){
        super();
        this.settings=startSettings.copy();
        this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));


        JPanel jp=new JPanel();
        jp.setLayout(new BoxLayout(jp,BoxLayout.LINE_AXIS));
        JLabel jl=new JLabel("GridWidth: "+settings.getGridWidth());
        jp.add(jl);
        JSlider js=new JSlider();
        js.setMinimum(2);
        js.setMaximum(main.getWidth());
        js.addChangeListener(e -> {
            settings.setGridWidth(((JSlider)e.getSource()).getValue());
            jl.setText("GridHeight: "+settings.getGridWidth());
        });
        jp.add(js);
        this.add(jp);
        JPanel jp2=new JPanel();
        jp2.setLayout(new BoxLayout(jp2,BoxLayout.LINE_AXIS));
        JLabel jl2=new JLabel("GridHeight: "+settings.getGridHeight());
        jp2.add(jl2);
        JSlider js2=new JSlider();
        js2.setMinimum(2);
        js2.setMaximum(main.getHeight());
        js2.addChangeListener(e -> {
            settings.setGridHeight(((JSlider)e.getSource()).getValue());
            jl2.setText("GridHeight: "+settings.getGridHeight());
        });
        jp2.add(js2);
        this.add(jp2);


        JPanel jp3=new JPanel();
        jp3.setLayout(new BoxLayout(jp3,BoxLayout.LINE_AXIS));
        JLabel jl3=new JLabel("#dots: "+settings.getDots());
        jp3.add(jl3);
        JSlider js3=new JSlider();
        js3.setMinimum(0);
        js3.setMaximum(this.settings.getGridHeight()*this.settings.getGridWidth());
        js3.addChangeListener(e -> {
            settings.setDots(((JSlider)e.getSource()).getValue());
            jl3.setText("#dots: "+settings.getDots());
        });
        jp3.add(js3);
        this.add(jp3);
        JPanel jp4=new JPanel();
        jp4.setLayout(new BoxLayout(jp4,BoxLayout.LINE_AXIS));
        JButton pauseButton=new JButton("▍ ▍");
        pauseButton.addActionListener(e -> {
            boolean isPaused=main.togglePaused();
            pauseButton.setText(isPaused? "▶" : "▍ ▍");
        });
        jp4.add(pauseButton);
        JButton changeButton=new JButton("change");
        changeButton.addActionListener(e -> {
            main.setSettings(settings);
            pauseButton.setText("▍ ▍");
        });
        jp4.add(changeButton);
        this.add(jp4);
    }
}
