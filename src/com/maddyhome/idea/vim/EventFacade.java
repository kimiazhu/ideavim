/*
 * IdeaVim - Vim emulator for IDEs based on the IntelliJ platform
 * Copyright (C) 2003-2016 The IdeaVim authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.maddyhome.idea.vim;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.ShortcutSet;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.editor.event.*;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author vlan
 */
public class EventFacade {
  @NotNull private static final EventFacade ourInstance = new EventFacade();

  @Nullable private TypedActionHandler myOriginalTypedActionHandler;

  private EventFacade() {
  }

  @NotNull
  public static EventFacade getInstance() {
    return ourInstance;
  }

  public void addProjectManagerListener(@NotNull ProjectManagerListener listener) {
    ProjectManager.getInstance().addProjectManagerListener(listener);
  }

  public void setupTypedActionHandler(@NotNull TypedActionHandler handler) {
    final TypedAction typedAction = getTypedAction();
    myOriginalTypedActionHandler = typedAction.getRawHandler();
    typedAction.setupRawHandler(handler);
  }

  public void restoreTypedActionHandler() {
    if (myOriginalTypedActionHandler != null) {
      getTypedAction().setupRawHandler(myOriginalTypedActionHandler);
    }
  }

  public void registerCustomShortcutSet(@NotNull AnAction action, @NotNull ShortcutSet shortcutSet,
                                        @Nullable JComponent component) {
    action.registerCustomShortcutSet(shortcutSet, component);
  }

  public void unregisterCustomShortcutSet(@NotNull AnAction action, @Nullable JComponent component) {
    action.unregisterCustomShortcutSet(component);
  }

  public void addFileEditorManagerListener(@NotNull Project project, @NotNull FileEditorManagerListener listener) {
    final MessageBusConnection connection = project.getMessageBus().connect();
    connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, listener);
  }

  public void addDocumentListener(@NotNull Document document, @NotNull DocumentListener listener) {
    document.addDocumentListener(listener);
  }

  public void removeDocumentListener(@NotNull Document document, @NotNull DocumentListener listener) {
    document.removeDocumentListener(listener);
  }

  public void addEditorFactoryListener(@NotNull EditorFactoryListener listener, @NotNull Disposable parentDisposable) {
    EditorFactory.getInstance().addEditorFactoryListener(listener, parentDisposable);
  }

  public void addEditorMouseListener(@NotNull Editor editor, @NotNull EditorMouseListener listener) {
    editor.addEditorMouseListener(listener);
  }

  public void removeEditorMouseListener(@NotNull Editor editor, @NotNull EditorMouseListener listener) {
    editor.removeEditorMouseListener(listener);
  }

  public void addEditorMouseMotionListener(@NotNull Editor editor, @NotNull EditorMouseMotionListener listener) {
    editor.addEditorMouseMotionListener(listener);
  }

  public void removeEditorMouseMotionListener(@NotNull Editor editor, @NotNull EditorMouseMotionListener listener) {
    editor.removeEditorMouseMotionListener(listener);
  }

  public void addEditorSelectionListener(@NotNull Editor editor, @NotNull SelectionListener listener) {
    editor.getSelectionModel().addSelectionListener(listener);
  }

  public void removeEditorSelectionListener(@NotNull Editor editor, @NotNull SelectionListener listener) {
    editor.getSelectionModel().removeSelectionListener(listener);
  }

  @NotNull
  private TypedAction getTypedAction() {
    return EditorActionManager.getInstance().getTypedAction();
  }
}
