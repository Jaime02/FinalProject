package acousticfield3d.gui.panels;

import acousticfield3d.gui.MainForm;
import acousticfield3d.scene.Entity;
import acousticfield3d.scene.MeshEntity;
import acousticfield3d.scene.Resources;
import acousticfield3d.utils.Color;

public class MiscPanel extends javax.swing.JPanel {
    public MainForm mf;
    
    public MiscPanel(MainForm mf) {
        this.mf = mf;
        initComponents();
    }
  
    private void initComponents() {
        maskGroup = new javax.swing.ButtonGroup();
        shadersTypeGroup = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        maskAddCubeButton = new javax.swing.JButton();
        maskDelButton = new javax.swing.JButton();
        maskAddSphereButton = new javax.swing.JButton();
        colorText = new javax.swing.JTextField();
        reloadShadersButton = new javax.swing.JButton();

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("mask objects"));

        maskAddCubeButton.setText("Cube");
        maskAddCubeButton.setToolTipText("Create a cube");
        maskAddCubeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maskAddCubeButtonActionPerformed(evt);
            }
        });

        maskDelButton.setText("Delete");
        maskDelButton.setToolTipText("Deletes the selected object");
        maskDelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maskDelButtonActionPerformed(evt);
            }
        });

        maskAddSphereButton.setText("Sphere");
        maskAddSphereButton.setToolTipText("Create a sphere");
        maskAddSphereButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maskAddSphereButtonActionPerformed(evt);
            }
        });

        colorText.setText("255,255,255,255");
        colorText.setToolTipText("Color RGBA");
        colorText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorTextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorText)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(maskAddCubeButton)
                        .addGap(6, 6, 6)
                        .addComponent(maskAddSphereButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(maskDelButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maskAddCubeButton)
                    .addComponent(maskAddSphereButton)
                    .addComponent(maskDelButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(colorText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        reloadShadersButton.setText("ReloadShaders");
        reloadShadersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadShadersButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(reloadShadersButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(reloadShadersButton)
                .addContainerGap())
        );
    }

    private void maskAddCubeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        MeshEntity me = new MeshEntity(Resources.MESH_BOX, Resources.SHADER_SOLID_SPEC);
        me.setTag( Entity.TAG_OBJ ); me.setColor( Color.WHITE );
        mf.addMeshEntityToSceneCenterAndResizeIt(me);
        
        mf.needUpdate();
    }

    private void maskDelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        //Remove from simulation and scene
        for (Entity e : mf.selection){
            if (e.getTag() == Entity.TAG_OBJ){
                mf.scene.getEntities().remove( e );
            }
        }
        mf.clearSelection();
        mf.needUpdate();
    }

    private void maskAddSphereButtonActionPerformed(java.awt.event.ActionEvent evt) {
        MeshEntity me = new MeshEntity(Resources.MESH_SPHERE, Resources.SHADER_SOLID_SPEC);
        me.setTag( Entity.TAG_OBJ ); me.setColor( Color.WHITE );
        mf.addMeshEntityToSceneCenterAndResizeIt(me);
        mf.needUpdate();
    }

    private void reloadShadersButtonActionPerformed(java.awt.event.ActionEvent evt) {
        mf.renderer.reloadShaders();
    }

    private void colorTextActionPerformed(java.awt.event.ActionEvent evt) {
        int color = Color.parse( colorText.getText() );
        for (Entity e : mf.selection){
            if (e instanceof MeshEntity){
                MeshEntity me = (MeshEntity)e;
                me.setColor( color );
            }
        }
        mf.needUpdate();
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField colorText;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton maskAddCubeButton;
    private javax.swing.JButton maskAddSphereButton;
    private javax.swing.JButton maskDelButton;
    private javax.swing.ButtonGroup maskGroup;
    private javax.swing.JButton reloadShadersButton;
    private javax.swing.ButtonGroup shadersTypeGroup;
    // End of variables declaration//GEN-END:variables
}
