/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.maps;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author geoma48p
 */
public abstract class AbstractMapPanel extends JPanel {

   public abstract JLabel getQueueLabel();

   public abstract JPanel getPanel();

}
