// ... Весь тот же код до Scaffold(...) остаётся прежним

        var searchQuery by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = { Text("CodeEditor98 — ") },
                        actions = {
                            Row {
                                Text("Lines")
                                Switch(checked = lineNumbers, onCheckedChange = {
                                    lineNumbers = it
                                    savePref(context, "lineNumbers", it)
                                    editorView?.isLineNumberEnabled = it
                                })
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Wrap")
                                Switch(checked = wordWrap, onCheckedChange = {
                                    wordWrap = it
                                    savePref(context, "wordWrap", it)
                                    editorView?.isWordwrap = it
                                })
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (isDarkTheme.value) "Dark" else "Light")
                                Switch(checked = isDarkTheme.value, onCheckedChange = {
                                    isDarkTheme.value = it
                                    savePref(context, "darkTheme", it)
                                })
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = { openLauncher.launch(arrayOf("*/*")) }) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = { editorView?.undo() }) { Text("Undo") }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = { editorView?.redo() }) { Text("Redo") }

                                    Text("Open")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = {
                                    codeText = editorView?.text.toString()
                                    if (fileUri != null) {
                                        context.contentResolver.openOutputStream(fileUri!!)?.let { out ->
                                            OutputStreamWriter(out).use { writer -> writer.write(codeText) }
                                        }
                                    } else {
                                        saveLauncher.launch("untitled.txt")
                                    }
                                }) {
                                    Text("Save")
                                }
                            }
                        }
                    )
                    Row(modifier = Modifier.padding(8.dp)) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Search") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            editorView?.findNext(searchQuery)
                        }) {
                            Text("Find")
                        }
                    }
                }
            }
        ) { padding ->
            AndroidView(
                factory = { ctx ->
                    CodeEditorView(ctx).apply {
                        setEditorLanguage(LanguageText())
                        setText(codeText)
                        isLineNumberEnabled = lineNumbers
                        isWordwrap = wordWrap
                        editorView = this
                    }
                },
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            )
        }

        // ... Остальная часть кода (autoDetectLanguage, loadPref, savePref) — без изменений
