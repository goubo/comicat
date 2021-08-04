// src/main.js
const { app, BrowserWindow } = require('electron')

app.allowRendererProcessReuse = true

function createWindow () {
    let win = new BrowserWindow({
        height: 500,
        width: 800,
        webPreferences: {
            nodeIntegration: true
        }
    })

    win.loadURL(`http://localhost:3000`)
}

app.on('ready', () => createWindow())
