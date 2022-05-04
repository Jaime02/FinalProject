package acousticfield3d.gui;

import acousticfield3d.utils.DialogUtils;
import acousticfield3d.utils.FileUtils;
import acousticfield3d.gui.panels.MiscPanel;
import acousticfield3d.gui.panels.MovePanel;
import acousticfield3d.math.M;
import acousticfield3d.math.Quaternion;
import acousticfield3d.math.Transform;
import acousticfield3d.math.Vector3f;
import acousticfield3d.renderer.Renderer;
import acousticfield3d.scene.Entity;
import acousticfield3d.scene.MeshEntity;
import acousticfield3d.scene.Resources;
import acousticfield3d.scene.Scene;
import acousticfield3d.scene.SceneObjExport;
import acousticfield3d.utils.Color;
import acousticfield3d.utils.Parse;
import acousticfield3d.utils.StringFormats;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import javax.swing.JFrame;
import javax.swing.JTextField;

public final class MainForm extends javax.swing.JFrame {
    public final ArrayList<Entity> selection = new ArrayList<>();
    public final ArrayList<Entity> bag = new ArrayList<>();
    boolean cameraLooked;
    boolean hasDragged;
    int firstDragX, firstDragY;
    String lastSimulationSavedOrLoaded;

    public final Renderer renderer;
    public final Scene scene;


    public GLJPanel panel;

    public final MiscPanel miscPanel;
    public final MovePanel movePanel;

    public MainForm() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cameraLooked = true;

        scene = new Scene();
        miscPanel = new MiscPanel(this);
        renderer = new Renderer(scene, this);
        movePanel = new MovePanel(this);

        initComponents();
        initSimulation();
        mainTabPanel.addTab("Misc", miscPanel);
        mainTabPanel.addTab("Move", movePanel);
    }

    public void initSimulation() {
        // remove old transducers
        Scene.removeWithTag(scene.getEntities(), Entity.TAG_TRANSDUCER);
        // remove old Control Points
        Scene.removeWithTag(scene.getEntities(), Entity.TAG_CONTROL_POINT);
        // remove old Masks
        Scene.removeWithTag(scene.getEntities(), Entity.TAG_OBJ);
        // remove old slices
        Scene.removeWithTag(scene.getEntities(), Entity.TAG_SLICE);

        // just add a basic cube and sphere
        MeshEntity me = new MeshEntity(Resources.MESH_BOX, Resources.SHADER_SOLID_SPEC);
        me.getTransform().getScale().set(0.1f);
        me.setTag(Entity.TAG_OBJ);
        me.setColor(Color.RED);
        scene.getEntities().add(me);

        MeshEntity me2 = new MeshEntity(Resources.MESH_SPHERE, Resources.SHADER_SOLID_SPEC);
        me2.getTransform().getScale().set(0.12f);
        me2.getTransform().getTranslation().set(0.1f, 0, 0);
        me2.setTag(Entity.TAG_OBJ);
        me2.setColor(Color.BLUE);
        scene.getEntities().add(me2);

        adjustGUIGainAndCameras();

        needUpdate();
    }

    public void adjustGUIGainAndCameras() {
        scene.updateSimulationBoundaries();

        // init camera
        scene.adjustCameraToSimulation(scene, getGLAspect());
        scene.adjustCameraToSimulation(scene, getGLAspect());
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        mainTabPanel = new javax.swing.JTabbedPane();
        sliderFieldLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        rzText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        rxText = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        syText = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        xText = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        szText = new javax.swing.JTextField();
        ryText = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        sxText = new javax.swing.JTextField();
        yText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        zText = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        containerPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        loadSimMenu = new javax.swing.JMenuItem();
        saveSimMenu = new javax.swing.JMenuItem();
        saveSameFileMenu = new javax.swing.JMenuItem();
        loadLastMenu = new javax.swing.JMenuItem();
        exportObjMenu = new javax.swing.JMenuItem();
        exportObjWithMtlMenu = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        simulationResizeMenu = new javax.swing.JMenuItem();
        recToSelMenu = new javax.swing.JMenuItem();
        selToBagMenu = new javax.swing.JMenuItem();
        simTransformMenu = new javax.swing.JMenuItem();
        assignSel2Menu = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        sel1Menu = new javax.swing.JMenuItem();
        sel2Menu = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        zoomInMenu = new javax.swing.JMenuItem();
        zoomOutMenu = new javax.swing.JMenuItem();
        camViewMenu = new javax.swing.JMenuItem();
        resetCamMenu = new javax.swing.JMenuItem();
        unlockCameraMenu = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        camLookSelectionMenu = new javax.swing.JMenuItem();
        originCamMenu = new javax.swing.JMenuItem();
        centerCamMenu = new javax.swing.JMenuItem();
        otherCamMenu = new javax.swing.JMenuItem();
        camCoverSelMenu = new javax.swing.JMenuItem();
        cameraMovMenu = new javax.swing.JMenuItem();

        setTitle("LiveGL");

        rzText.setText("0");
        rzText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                rzTextFocusGained(evt);
            }
        });
        rzText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rzTextActionPerformed(evt);
            }
        });

        jLabel1.setText("X");

        jLabel6.setText("RY");

        rxText.setText("0");
        rxText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                rxTextFocusGained(evt);
            }
        });
        rxText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rxTextActionPerformed(evt);
            }
        });

        jLabel5.setText("RZ");

        jLabel2.setText("Y");

        syText.setText("0");
        syText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                syTextFocusGained(evt);
            }
        });
        syText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                syTextActionPerformed(evt);
            }
        });

        jLabel21.setText("SZ:");

        xText.setText("0");
        xText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                xTextFocusGained(evt);
            }
        });
        xText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xTextActionPerformed(evt);
            }
        });

        jLabel15.setText("SX:");

        szText.setText("0");
        szText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                szTextFocusGained(evt);
            }
        });
        szText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                szTextActionPerformed(evt);
            }
        });

        ryText.setText("0");
        ryText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ryTextFocusGained(evt);
            }
        });
        ryText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ryTextActionPerformed(evt);
            }
        });

        jLabel4.setText("RX");

        sxText.setText("0");
        sxText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                sxTextFocusGained(evt);
            }
        });
        sxText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sxTextActionPerformed(evt);
            }
        });

        yText.setText("0");
        yText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                yTextFocusGained(evt);
            }
        });
        yText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yTextActionPerformed(evt);
            }
        });

        jLabel3.setText("Z");

        zText.setText("0");
        zText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                zTextFocusGained(evt);
            }
        });
        zText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zTextActionPerformed(evt);
            }
        });

        jLabel16.setText("SY:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel21)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(szText, javax.swing.GroupLayout.DEFAULT_SIZE, 81,
                                                        Short.MAX_VALUE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel15)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(sxText))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(zText))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(yText))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(xText)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel16)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(syText, javax.swing.GroupLayout.DEFAULT_SIZE, 81,
                                                        Short.MAX_VALUE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(rzText))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel6)
                                                        .addComponent(jLabel4))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(rxText)
                                                        .addComponent(ryText))))
                                .addContainerGap()));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel2Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(jLabel1)
                                                                .addComponent(xText,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(rxText, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2)
                                                        .addGroup(jPanel2Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(yText,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(jLabel6)
                                                                .addComponent(ryText,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(zText, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5)
                                        .addComponent(rzText, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel15)
                                        .addComponent(sxText, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel16)
                                        .addComponent(syText, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel21)
                                        .addComponent(szText, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap()));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainTabPanel, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(sliderFieldLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap()));

        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(sliderFieldLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainTabPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 350,
                                        Short.MAX_VALUE)));

        containerPanel.setLayout(new java.awt.BorderLayout());

        GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities(glprofile);
        panel = new GLJPanel(glcapabilities);
        
        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);

        panel.addGLEventListener(new GLEventListener() {
            @Override
            public void init(GLAutoDrawable glautodrawable) {
                renderer.init(glautodrawable.getGL().getGL2(),
                        glautodrawable.getSurfaceWidth(), glautodrawable.getSurfaceHeight());
            }

            @Override
            public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {
                renderer.reshape(glautodrawable.getGL().getGL2(), width, height);
            }

            @Override
            public void dispose(GLAutoDrawable glautodrawable) {
                renderer.dispose(glautodrawable.getGL().getGL2());
            }

            @Override
            public void display(GLAutoDrawable glautodrawable) {
                // TimerUtil.get().tack("Render");
                // TimerUtil.get().tick("Render");
                renderer.render(glautodrawable.getGL().getGL2(),
                        glautodrawable.getSurfaceWidth(), glautodrawable.getSurfaceHeight());
            }
        });

        panel.setBackground(new java.awt.Color(0, 0, 0));
        panel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panelMouseDragged(evt);
            }
        });

        panel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                panelMouseWheelMoved(evt);
            }
        });

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelMousePressed(evt);
            }
        });

        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 502, Short.MAX_VALUE));
        panelLayout.setVerticalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE));

        containerPanel.add(panel, java.awt.BorderLayout.CENTER);

        jMenu1.setText("File");

        loadSimMenu.setText("Load");
        loadSimMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadSimMenuActionPerformed(evt);
            }
        });
        jMenu1.add(loadSimMenu);

        saveSimMenu.setText("Save as");
        saveSimMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveSimMenuActionPerformed(evt);
            }
        });
        jMenu1.add(saveSimMenu);

        saveSameFileMenu.setAccelerator(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        saveSameFileMenu.setText("Save");
        saveSameFileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveSameFileMenuActionPerformed(evt);
            }
        });
        jMenu1.add(saveSameFileMenu);

        loadLastMenu.setAccelerator(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        loadLastMenu.setText("Load last");
        loadLastMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadLastMenuActionPerformed(evt);
            }
        });
        jMenu1.add(loadLastMenu);

        exportObjMenu.setText("Export to obj");
        exportObjMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportObjMenuActionPerformed(evt);
            }
        });
        jMenu1.add(exportObjMenu);

        exportObjWithMtlMenu.setText("ExportToObjWithMtl");
        exportObjWithMtlMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportObjWithMtlMenuActionPerformed(evt);
            }
        });
        jMenu1.add(exportObjWithMtlMenu);

        jMenuBar1.add(jMenu1);

        jMenu5.setText("Simulation");

        simulationResizeMenu.setText("Resize");
        simulationResizeMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulationResizeMenuActionPerformed(evt);
            }
        });
        jMenu5.add(simulationResizeMenu);

        recToSelMenu.setText("Recenter to sel");
        recToSelMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recToSelMenuActionPerformed(evt);
            }
        });
        jMenu5.add(recToSelMenu);

        selToBagMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        selToBagMenu.setText("Add sel to bag");
        selToBagMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selToBagMenuActionPerformed(evt);
            }
        });
        jMenu5.add(selToBagMenu);

        simTransformMenu.setText("Transform All");
        simTransformMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simTransformMenuActionPerformed(evt);
            }
        });
        jMenu5.add(simTransformMenu);

        assignSel2Menu.setText("assign sel 1");
        assignSel2Menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assignSel2MenuActionPerformed(evt);
            }
        });
        jMenu5.add(assignSel2Menu);

        jMenuItem2.setText("assign sel 2");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem2);

        sel1Menu.setText("sel 1");
        sel1Menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sel1MenuActionPerformed(evt);
            }
        });
        jMenu5.add(sel1Menu);

        sel2Menu.setText("sel 2");
        sel2Menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sel2MenuActionPerformed(evt);
            }
        });
        jMenu5.add(sel2Menu);

        jMenuBar1.add(jMenu5);

        jMenu2.setText("Camera");

        zoomInMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, 0));
        zoomInMenu.setText("Zoom in");
        zoomInMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomInMenuActionPerformed(evt);
            }
        });
        jMenu2.add(zoomInMenu);

        zoomOutMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, 0));
        zoomOutMenu.setText("Zoom out");
        zoomOutMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomOutMenuActionPerformed(evt);
            }
        });
        jMenu2.add(zoomOutMenu);

        camViewMenu.setText("Edit View");
        camViewMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                camViewMenuActionPerformed(evt);
            }
        });
        jMenu2.add(camViewMenu);

        resetCamMenu.setText("reset");
        resetCamMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetCamMenuActionPerformed(evt);
            }
        });
        jMenu2.add(resetCamMenu);

        unlockCameraMenu.setText("Un/Lock cam");
        unlockCameraMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unlockCameraMenuActionPerformed(evt);
            }
        });
        jMenu2.add(unlockCameraMenu);

        jMenu3.setText("Look At");

        camLookSelectionMenu.setText("Selection");
        camLookSelectionMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                camLookSelectionMenuActionPerformed(evt);
            }
        });
        jMenu3.add(camLookSelectionMenu);

        originCamMenu.setText("Origin 000");
        originCamMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                originCamMenuActionPerformed(evt);
            }
        });
        jMenu3.add(originCamMenu);

        centerCamMenu.setText("Center");
        centerCamMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                centerCamMenuActionPerformed(evt);
            }
        });
        jMenu3.add(centerCamMenu);

        otherCamMenu.setText("Other");
        otherCamMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otherCamMenuActionPerformed(evt);
            }
        });
        jMenu3.add(otherCamMenu);

        jMenu2.add(jMenu3);

        camCoverSelMenu.setText("cover sel");
        camCoverSelMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                camCoverSelMenuActionPerformed(evt);
            }
        });
        jMenu2.add(camCoverSelMenu);

        cameraMovMenu.setText("CameraMov");
        cameraMovMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cameraMovMenuActionPerformed(evt);
            }
        });
        jMenu2.add(cameraMovMenu);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(containerPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(containerPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
    }

    private int lastButton, lastX, lastY;

    private void panelMousePressed(java.awt.event.MouseEvent evt) {
        lastButton = evt.getButton();
        firstDragX = lastX = evt.getX();
        firstDragY = lastY = evt.getY();

        if (lastButton == 3) {
            if (cameraLooked) {
                scene.getCamera().activateObservation(true, scene.getCamera().getObservationPoint());
            }
        } else if (lastButton == 1) {
            updateSelection(evt);
        }
    }

    private void panelMouseDragged(java.awt.event.MouseEvent evt) {
        int x = evt.getX();
        int y = evt.getY();
        final float rotGain = 0.01f;
        float diffX = (x - lastX);
        float diffY = (y - lastY);

        if (lastButton == 1) {
            updateSelection(evt);

        } else if (lastButton == 3) {
            if (cameraLooked) {
                scene.getCamera().moveAzimuthAndInclination(-diffX * rotGain, -diffY * rotGain);
                scene.getCamera().updateObservation();
            } else {
                scene.getCamera().getTransform().rotateLocal(-diffY * rotGain, -diffX * rotGain, 0);
            }
        } else if (lastButton == 2) {
            // scene.getCamera().getTransform().moveLocalSpace(-diffX * moveGain, diffY *
            // moveGain, 0);
            zoom(diffY * 1.5f);
        }

        needUpdate();
        lastX = x;
        lastY = y;
    }

    private void panelMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
        float wheel = (float) evt.getPreciseWheelRotation();
        final float wheelGain = 6f;
        final float value = wheel * wheelGain;
        zoom(value);
    }

    private void zoom(final float value) {
        if (cameraLooked) {
            scene.getCamera().setDistance(scene.getCamera().getDistance() + value);
            scene.getCamera().updateObservation();
        } else {
            scene.getCamera().getTransform().moveLocalSpace(0, 0, value);
        }
        needUpdate();
    }

    private void lookCamera(Vector3f v) {
        scene.getCamera().setOrtho(false);
        scene.getCamera().updateProjection(getGLAspect());
        scene.getCamera().activateObservation(true, v);
    }

    private void originCamMenuActionPerformed(java.awt.event.ActionEvent evt) {
        lookCamera(Vector3f.ZERO);
    }

    private void centerCamMenuActionPerformed(java.awt.event.ActionEvent evt) {
        lookCamera(scene.getSimulationCenter());
    }

    private void otherCamMenuActionPerformed(java.awt.event.ActionEvent evt) {
        String v = DialogUtils.getStringDialog(this, "Vector", "0.00 0.00 0.00");
        if (v != null) {
            lookCamera(new Vector3f().parse(v));
        }
    }

    public float getGLAspect() {
        return panel.getWidth() / (float) panel.getHeight();
    }

    private void loadSimMenuActionPerformed(java.awt.event.ActionEvent evt) {
        String target = FileUtils.selectFile(this, "open", ".xml.gz", null);
        if (target != null) {
            loadSimulation(target);
            lastSimulationSavedOrLoaded = target;
        }
    }

    public void loadSimulation(String target) {
        try {
            scene.getEntities().clear();
            Scene newScene = (Scene) FileUtils.readCompressedObject(new File(target));
            scene.getEntities().addAll(newScene.getEntities());
            initSimulation();
            clearSelection();

            needUpdate();
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearSelection() {
        for (Entity e : selection) {
            e.selected = false;
        }
        selection.clear();
    }

    public void setSelection(Entity e) {
        clearSelection();
        e.selected = true;
        selection.add(e);
    }

    public void addToSelection(Entity e) {
        e.selected = true;
        selection.add(e);
    }

    private void saveSimMenuActionPerformed(java.awt.event.ActionEvent evt) {
        String file = FileUtils.selectNonExistingFile(this, ".xml.gz");
        if (file != null) {
            saveSimulation(file);
        }
    }

    private void saveSimulation(final String file) {
        try {

            FileUtils.writeCompressedObject(new File(file), scene);
            lastSimulationSavedOrLoaded = file;
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void resetCamMenuActionPerformed(java.awt.event.ActionEvent evt) {
        scene.adjustCameraToSimulation(scene, getGLAspect());
        needUpdate();
    }

    private void xTextFocusGained(java.awt.event.FocusEvent evt) {
        changeSlider(FieldsToChange.xField, "X", scene.getMinSize() * 8.0f);
    }

    private void yTextFocusGained(java.awt.event.FocusEvent evt) {
        changeSlider(FieldsToChange.yField, "Y", scene.getMinSize() * 8.0f);
    }

    private void zTextFocusGained(java.awt.event.FocusEvent evt) {
        changeSlider(FieldsToChange.zField, "Z", scene.getMinSize() * 8.0f);
    }

    private void rxTextFocusGained(java.awt.event.FocusEvent evt) {
        changeSlider(FieldsToChange.rxField, "RX", 360);
    }

    private void ryTextFocusGained(java.awt.event.FocusEvent evt) {
        changeSlider(FieldsToChange.ryField, "RY", 360);
    }

    private void rzTextFocusGained(java.awt.event.FocusEvent evt) {
        changeSlider(FieldsToChange.rzField, "RZ", 360);
    }

    public void updateTransForField(FieldsToChange field, String text) {
        if (text.length() < 1) {
            return;
        }
        boolean absolute;
        float value;
        if (text.charAt(0) == 'a') {
            absolute = false;
            value = Parse.toFloat(text.substring(1));
        } else {
            absolute = true;
            value = Parse.toFloat(text);
        }
        changeSelectionField(field, value, absolute, false);
        needUpdate();
    }

    private void xTextActionPerformed(java.awt.event.ActionEvent evt) {
        updateTransForField(FieldsToChange.xField, xText.getText());
    }

    private void rxTextActionPerformed(java.awt.event.ActionEvent evt) {
        updateTransForField(FieldsToChange.rxField, rxText.getText());
    }

    private void yTextActionPerformed(java.awt.event.ActionEvent evt) {
        updateTransForField(FieldsToChange.yField, yText.getText());
    }

    private void ryTextActionPerformed(java.awt.event.ActionEvent evt) {
        updateTransForField(FieldsToChange.ryField, ryText.getText());
    }

    private void zTextActionPerformed(java.awt.event.ActionEvent evt) {
        updateTransForField(FieldsToChange.zField, zText.getText());
    }

    private void rzTextActionPerformed(java.awt.event.ActionEvent evt) {
        updateTransForField(FieldsToChange.rzField, rzText.getText());
    }

    private void syTextActionPerformed(java.awt.event.ActionEvent evt) {
        updateTransForField(FieldsToChange.syField, syText.getText());
    }

    private void syTextFocusGained(java.awt.event.FocusEvent evt) {
        changeSlider(FieldsToChange.syField, "SY", scene.maxDistanceBoundary() / 8.0f);
    }

    private void sxTextActionPerformed(java.awt.event.ActionEvent evt) {
        updateTransForField(FieldsToChange.sxField, sxText.getText());
    }

    private void sxTextFocusGained(java.awt.event.FocusEvent evt) {
        changeSlider(FieldsToChange.sxField, "SX", scene.maxDistanceBoundary() / 8.0f);
    }

    private void szTextFocusGained(java.awt.event.FocusEvent evt) {
        changeSlider(FieldsToChange.szField, "SZ", scene.maxDistanceBoundary() / 8.0f);
    }

    private void szTextActionPerformed(java.awt.event.ActionEvent evt) {
        updateTransForField(FieldsToChange.szField, szText.getText());
    }

    public void addMeshEntityToSceneCenterAndResizeIt(MeshEntity me) {
        me.getTransform().getTranslation().set(scene.getSimulationCenter());
        me.getTransform().getScale().set(scene.maxDistanceBoundary());
        scene.getEntities().add(me);
    }

    private void camViewMenuActionPerformed(java.awt.event.ActionEvent evt) {
        showNewFrame(new TransformForm(scene.getCamera().getTransform(), this));
    }

    private void unlockCameraMenuActionPerformed(java.awt.event.ActionEvent evt) {
        cameraLooked = !cameraLooked;
    }

    private void camLookSelectionMenuActionPerformed(java.awt.event.ActionEvent evt) {
        if (!selection.isEmpty()) {
            lookCamera(selection.get(0).getTransform().getTranslation());
        }
    }

    private void exportObjMenuActionPerformed(java.awt.event.ActionEvent evt) {
        SceneObjExport soe = new SceneObjExport(this);
        soe.export(false);
    }

    private void camCoverSelMenuActionPerformed(java.awt.event.ActionEvent evt) {
        if (!selection.isEmpty()) {
            scene.adjustCameraToCover(selection.get(0));
            needUpdate();
        }
    }

    private void cameraMovMenuActionPerformed(java.awt.event.ActionEvent evt) {
        showNewFrame(new CameraMoveFrame(this));
    }

    private void exportObjWithMtlMenuActionPerformed(java.awt.event.ActionEvent evt) {
        SceneObjExport soe = new SceneObjExport(this);
        soe.export(true);
    }

    private void recToSelMenuActionPerformed(java.awt.event.ActionEvent evt) {
        if (selection.size() != 1) {
            return;
        }
        scene.recenterTo(scene.getEntities(), selection.get(0).getTransform().getTranslation().clone());
        needUpdate();
    }

    private void selToBagMenuActionPerformed(java.awt.event.ActionEvent evt) {
        bag.clear();
        bag.addAll(selection);
    }

    private void simTransformMenuActionPerformed(java.awt.event.ActionEvent evt) {
        ApplyTransformForm atf = new ApplyTransformForm(this);
        atf.setLocationRelativeTo(this);
        atf.setVisible(true);
    }

    private void assignSel2MenuActionPerformed(java.awt.event.ActionEvent evt) {
        movePanel.snapSelection(1);
    }

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
        movePanel.snapSelection(2);
    }

    private void sel1MenuActionPerformed(java.awt.event.ActionEvent evt) {
        movePanel.applySelection(1);
    }

    private void sel2MenuActionPerformed(java.awt.event.ActionEvent evt) {
        movePanel.applySelection(2);
    }

    private void zoomInMenuActionPerformed(java.awt.event.ActionEvent evt) {
        zoom(6);
    }

    private void zoomOutMenuActionPerformed(java.awt.event.ActionEvent evt) {
        zoom(-6);
    }

    private void simulationResizeMenuActionPerformed(java.awt.event.ActionEvent evt) {
        scene.updateSimulationBoundaries();
        adjustGUIGainAndCameras();
        needUpdate();
    }

    private void loadLastMenuActionPerformed(java.awt.event.ActionEvent evt) {
        String fileToUse = null;

        if (lastSimulationSavedOrLoaded != null) {
            fileToUse = lastSimulationSavedOrLoaded;
        }

        if (fileToUse != null) {
            final int load = DialogUtils.getBooleanDialog(this, "Do you want to load " + fileToUse);
            if (load == 1) {
                loadSimulation(fileToUse);
                lastSimulationSavedOrLoaded = fileToUse;
            }
        }
    }

    private void saveSameFileMenuActionPerformed(java.awt.event.ActionEvent evt) {
        if (lastSimulationSavedOrLoaded != null) {
            saveSimulation(lastSimulationSavedOrLoaded);
        }
    }

    private void showNewFrame(final JFrame frame) {
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem assignSel2Menu;
    private javax.swing.JMenuItem camCoverSelMenu;
    private javax.swing.JMenuItem camLookSelectionMenu;
    private javax.swing.JMenuItem camViewMenu;
    private javax.swing.JMenuItem cameraMovMenu;
    private javax.swing.JMenuItem centerCamMenu;
    private javax.swing.JPanel containerPanel;
    private javax.swing.JMenuItem exportObjMenu;
    private javax.swing.JMenuItem exportObjWithMtlMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JMenuItem loadLastMenu;
    private javax.swing.JMenuItem loadSimMenu;
    private javax.swing.JTabbedPane mainTabPanel;
    private javax.swing.JMenuItem originCamMenu;
    private javax.swing.JMenuItem otherCamMenu;
    private javax.swing.JMenuItem recToSelMenu;
    private javax.swing.JMenuItem resetCamMenu;
    private javax.swing.JTextField rxText;
    private javax.swing.JTextField ryText;
    private javax.swing.JTextField rzText;
    private javax.swing.JMenuItem saveSameFileMenu;
    private javax.swing.JMenuItem saveSimMenu;
    private javax.swing.JMenuItem sel1Menu;
    private javax.swing.JMenuItem sel2Menu;
    private javax.swing.JMenuItem selToBagMenu;
    private javax.swing.JMenuItem simTransformMenu;
    private javax.swing.JMenuItem simulationResizeMenu;
    private javax.swing.JLabel sliderFieldLabel;
    private javax.swing.JTextField sxText;
    private javax.swing.JTextField syText;
    private javax.swing.JTextField szText;
    private javax.swing.JMenuItem unlockCameraMenu;
    private javax.swing.JTextField xText;
    private javax.swing.JTextField yText;
    private javax.swing.JTextField zText;
    private javax.swing.JMenuItem zoomInMenu;
    private javax.swing.JMenuItem zoomOutMenu;

    public void needUpdate() {
        panel.repaint();
    }

    public ArrayList<Entity> selectWithDrag(final int sx, final int sy, final int ex, final int ey, final int tags) {
        final float panelWidth = panel.getWidth();
        final float panelHeight = panel.getHeight();

        return scene.pickObjectsWithDrag(
                sx / panelWidth, 1.0f - sy / panelHeight,
                ex / panelWidth, 1.0f - ey / panelHeight,
                tags);
    }

    public MeshEntity clickRaySelectEntity(final int x, final int y, final int tags) {
        return scene.pickObject(
                x / (float) panel.getWidth(),
                1.0f - y / (float) panel.getHeight(), tags);
    }

    public Vector3f clickRayIntersectObject(final MeshEntity e, final int x, final int y) {
        return scene.clickToObject(x / (float) panel.getWidth(), 1.0f - y / (float) panel.getHeight(), e);
    }

    private int addTagsForSelectionFilter(int tags) {
        final Component comp = mainTabPanel.getSelectedComponent();

        if (comp == miscPanel) {
            tags |= Entity.TAG_OBJ;
        } else if (comp == movePanel) {
            tags |= Entity.TAG_CONTROL_POINT;
        }

        return tags;
    }

    private void updateSelection(MouseEvent evt) {
        final int x = evt.getX();
        final int y = evt.getY();
        int tags = Entity.TAG_NONE;

        tags = addTagsForSelectionFilter(tags);

        Entity e = scene.pickObject(
                lastX / (float) panel.getWidth(),
                1.0f - lastY / (float) panel.getHeight(), tags);
        if (e == null) {
            clearSelection();
            needUpdate();
            return;
        }

        if ((evt.getModifiersEx() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK) {
            if (selection.contains(e)) {
                selection.remove(e);
                e.selected = false;
            } else {
                e.selected = true;
                selection.add(e);
                entityToGUI(e);
            }
        } else {
            clearSelection();
            e.selected = true;
            selection.add(e);
            entityToGUI(e);
        }

        needUpdate();
    }

    private void vectorToTextFields(final Vector3f v, JTextField x, JTextField y, JTextField z) {
        x.setText(StringFormats.get().dc4(v.x));
        y.setText(StringFormats.get().dc4(v.y));
        z.setText(StringFormats.get().dc4(v.z));
    }

    public void transformToGUI(final Transform t) {
        vectorToTextFields(t.getTranslation(), xText, yText, zText);
        final Vector3f angles = t.getRotation().toAngles(null).multLocal(M.RAD_TO_DEG);
        vectorToTextFields(angles, rxText, ryText, rzText);
        vectorToTextFields(t.getScale(), sxText, syText, szText);
    }

    private void entityToGUI(Entity e) {
        transformToGUI(e.getTransform());

    }

    public void setSelection(ArrayList<Entity> sel) {
        clearSelection();
        for (Entity e : sel) {
            e.selected = true;
            selection.add(e);
        }
    }

    public enum FieldsToChange {
        xField, yField, zField, rxField, ryField, rzField,
        sxField, syField, szField
    };

    private FieldsToChange sliderField;
    private float sliderScale;

    public void changeSlider(FieldsToChange field, String name, float scale) {
        sliderField = field;
        sliderFieldLabel.setText(name);
        sliderScale = scale;
    }

    private void changeSelectionField(FieldsToChange field, float value, boolean absolute, boolean updateTextField) {
        final Vector3f angles = new Vector3f();

        for (Entity e : selection) {
            Transform tra = e.getTransform();

            if (field == FieldsToChange.xField) {
                tra.getTranslation().x = absolute ? value : tra.getTranslation().x + value;
                if (updateTextField) {
                    xText.setText(StringFormats.get().dc4(tra.getTranslation().x));
                }
            } else if (field == FieldsToChange.yField) {
                tra.getTranslation().y = absolute ? value : tra.getTranslation().y + value;
                if (updateTextField) {
                    yText.setText(StringFormats.get().dc4(tra.getTranslation().y));
                }
            } else if (field == FieldsToChange.zField) {
                tra.getTranslation().z = absolute ? value : tra.getTranslation().z + value;
                if (updateTextField) {
                    zText.setText(StringFormats.get().dc4(tra.getTranslation().z));
                }
            } else if (field == FieldsToChange.sxField) {
                tra.getScale().x = absolute ? value : tra.getScale().x + value;
                if (updateTextField) {
                    sxText.setText(StringFormats.get().dc4(tra.getScale().x));
                }
            } else if (field == FieldsToChange.syField) {
                tra.getScale().y = absolute ? value : tra.getScale().y + value;
                if (updateTextField) {
                    syText.setText(StringFormats.get().dc4(tra.getScale().y));
                }
            } else if (field == FieldsToChange.szField) {
                tra.getScale().z = absolute ? value : tra.getScale().z + value;
                if (updateTextField) {
                    szText.setText(StringFormats.get().dc4(tra.getScale().z));
                }
            } else if (field == FieldsToChange.rxField ||
                    field == FieldsToChange.ryField ||
                    field == FieldsToChange.rzField) {
                float rads = value * M.DEG_TO_RAD;
                Quaternion q = tra.getRotation();
                q.toAngles(angles);
                if (field == FieldsToChange.rxField) {
                    angles.x = absolute ? rads : angles.x + rads;
                    if (updateTextField) {
                        rxText.setText(StringFormats.get().dc4(angles.x * M.RAD_TO_DEG));
                    }
                } else if (field == FieldsToChange.ryField) {
                    angles.y = absolute ? rads : angles.y + rads;
                    if (updateTextField) {
                        ryText.setText(StringFormats.get().dc4(angles.y * M.RAD_TO_DEG));
                    }
                } else if (field == FieldsToChange.rzField) {
                    angles.z = absolute ? rads : angles.z + rads;
                    if (updateTextField) {
                        rzText.setText(StringFormats.get().dc4(angles.z * M.RAD_TO_DEG));
                    }
                }
                q.fromAngles(angles);
            }

            updateTextField = false;       
        }
    }
}
