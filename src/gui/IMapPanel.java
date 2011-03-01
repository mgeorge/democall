/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author geoma48p
 */
public interface IMapPanel {

    public JLabel getQueueLabel();

    public JPanel getPanel();

    public LabelProcessor getProcessor();

}
