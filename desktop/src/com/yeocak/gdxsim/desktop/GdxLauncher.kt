package com.yeocak.gdxsim.desktop

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas
import com.badlogic.gdx.utils.reflect.ClassReflection
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*

class GdxLauncher : JFrame() {
    private var gdxList = JList(arrayOf(
        "com.yeocak.gdxsim.pendulum.PendulumMain",
        "com.yeocak.gdxsim.flow.FlowMain",
        "com.yeocak.gdxsim.noisecircle.NoiseCircle"))

    private val cellWidth = 300
    private val windowWidth = 1000 + cellWidth
    private val windowHeight = 1000

    private val windowSize = Dimension(windowWidth, windowHeight)

    private var lwjglAWTCanvas: LwjglAWTCanvas?= null

    init {
        title = "Gdx Simulation Launcher"
        minimumSize = windowSize
        size = windowSize
        isResizable = true
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        addWindowFocusListener(object : WindowAdapter(){
            override fun windowClosing(e: WindowEvent?) {
                lwjglAWTCanvas?.stop()
            }
        })

        createControlPanel()

        pack()
        isVisible = true
    }

    private fun createControlPanel(){
        val controlPanel = JPanel(GridBagLayout())
        val c = GridBagConstraints()

        c.gridx = 0
        c.gridy = 0
        c.fill = GridBagConstraints.VERTICAL
        c.weighty = 1.0

        gdxList.fixedCellWidth = cellWidth
        gdxList.selectionMode = ListSelectionModel.SINGLE_SELECTION

        val scrollPane = JScrollPane(gdxList)
        controlPanel.add(scrollPane, c)

        contentPane.add(controlPanel, BorderLayout.EAST)

        // Double Click to Launch Any Simulation
        gdxList.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                if(e?.clickCount == 2){
                    launchProject(gdxList.selectedValue)
                }
            }
        })
    }

    private fun launchProject(name: String){
        lwjglAWTCanvas?.stop()

        lwjglAWTCanvas?.let {
            contentPane.remove(lwjglAWTCanvas!!.canvas)
        }

        val reflectionByName = ClassReflection.forName(name)
        val launcher = ClassReflection.newInstance(reflectionByName) as ApplicationListener

        lwjglAWTCanvas = LwjglAWTCanvas(launcher)
        lwjglAWTCanvas?.canvas?.size = Dimension(windowWidth-cellWidth, windowHeight)
        contentPane.add(lwjglAWTCanvas?.canvas as Component , BorderLayout.CENTER)
        pack()
    }
}