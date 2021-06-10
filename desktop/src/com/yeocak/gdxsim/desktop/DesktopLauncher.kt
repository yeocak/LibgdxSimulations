package com.yeocak.gdxsim.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.yeocak.gdxsim.flow.FlowMain
import javax.swing.SwingUtilities


fun main(args: Array<String>) {
    SwingUtilities.invokeLater { GdxLauncher() }
}