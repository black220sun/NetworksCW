package org.blacksun.view

import org.blacksun.graph.channel.ChannelFactory
import org.blacksun.graph.node.GraphNode
import org.blacksun.network.Network
import org.blacksun.network.NetworkSummary
import org.blacksun.utils.Config
import org.blacksun.utils.WeightList

import javax.swing.*
import java.awt.*

class ToolbarPanel(private val networkPanel: NetworkPanel) : JPanel() {
    private val network: Network = networkPanel.network
    private val terminal: JCheckBox
    private val fromNode: JComboBox<GraphNode>
    private val toNode: JComboBox<GraphNode>
    private val timeLabel: JLabel
    private val infoArea: JTextArea
    private var time: Int = 0

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        val nodes = network.getNodes(true)!!.toTypedArray()
        fromNode = JComboBox(nodes)
        toNode = JComboBox(nodes)
        terminal = JCheckBox("Terminal", true)
        addNodePanel()
        addChangePanel()
        addInfoPanel()
        timeLabel = JLabel("0")
        addTimePanel()
        infoArea = JTextArea()
        infoArea.isEditable = false
        add(infoArea)
    }

    private fun addInfoPanel() {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        panel.add(createButton("Remove all") {
            network.clear()
            updateNodes()
            terminal.isSelected = false
            networkPanel.update()
        })
        panel.add(createButton("Generate") {
            Config.config.setProperty("counter", 0)
            network.generateNetwork()
            updateNodes()
            networkPanel.update()
        })
        panel.add(createButton("Show table") {
            val node = fromNode.selectedItem as GraphNode
            infoArea.text = network.getPaths(node)
                    .joinToString("\n") { it.toString() }
        })
        panel.add(createButton("Run test") {
            infoArea.text = ""
            network.closeAll()
            infoArea.text = "\t" + NetworkSummary.configOptions +
                    "\n\tVIRTUAL CHANNEL MODE " +
                    NetworkSummary(network).runTests(false) +
                    "\n\tDATAGRAM MODE " +
                    NetworkSummary(network).runTests(true)
        })
        panel.add(createButton("Clear") { infoArea.text = "" })
        add(panel)
    }

    private fun addTimePanel() {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        panel.add(timeLabel)
        panel.add(JLabel("        "))
        panel.add(createButton("Reset") {
            timeLabel.text = "0"
            time = 0
        })
        panel.add(createButton("Send", nodeAction { n1, n2 ->
            time += network.getPath(n1, n2, true).weight
            timeLabel.text = time.toString()
        }))
        panel.add(createButton("Settings") {  SettingsFrame() })
        panel.add(createButton("Update") { networkPanel.update() })
        add(panel)
    }

    private fun addChangePanel() {
        val pane = JPanel()
        pane.layout = BoxLayout(pane, BoxLayout.X_AXIS)
        pane.add(createButton("Terminal", nodeAction { n1, _ ->
            n1.isTerminal = !n1.isTerminal
            updateNodes()
        }))
        pane.add(createButton("Select", nodeAction { from, to ->
            val current = from.isSelected
            if (current == to.isSelected) {
                val changed = !current
                network.getPath(from, to).forEach { ch -> ch.isSelected = changed }
                from.isSelected = changed
                to.isSelected = changed
            }
        }))
        pane.add(createButton("Add") {
            network.addNode()
            updateNodes()
            networkPanel.update()
        })
        pane.add(createButton("Remove", nodeAction { node, _ ->
            network.removeNode(node)
            updateNodes()
        }))
        pane.add(createButton("Link", nodeAction { n1, n2 ->
            val cfg = Config.config
            val weight = cfg.getProperty<WeightList>("weights").weight
            cfg.getProperty<ChannelFactory>("channelFactory").createChannel(n1, n2, weight)
        }))
        pane.add(createButton("Unlink", nodeAction { from, to ->
            network.removeConnection(from, to) }))
        add(pane)
    }

    private fun addNodePanel() {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        panel.add(terminal)
        terminal.addChangeListener { updateNodes() }
        panel.add(fromNode)
        panel.add(toNode)
        panel.add(createButton("Connect", nodeAction { from, to ->
            network.createConnection(from, to)
        }))
        panel.add(createButton("Disconnect", nodeAction { from, to ->
            network.closeConnection(from, to)
        }))
        panel.add(createButton("Close all") {
            network.closeAll()
            networkPanel.update()
        })
        add(panel)
    }

    private fun createButton(name: String, action: () -> Unit): Component {
        val button = JButton(name)
        button.addActionListener { action() }
        return button
    }

    private fun updateNodes() {
        fromNode.removeAllItems()
        toNode.removeAllItems()
        network.getNodes(terminal.isSelected)!!.forEach { node ->
            fromNode.addItem(node)
            toNode.addItem(node)
        }
    }

    private fun nodeAction(consumer: (GraphNode, GraphNode) -> Unit): () -> Unit {
        return {
            val from = fromNode.selectedItem as GraphNode
            val to = toNode.selectedItem as GraphNode
            consumer(from, to)
            networkPanel.update()
        }
    }
}
