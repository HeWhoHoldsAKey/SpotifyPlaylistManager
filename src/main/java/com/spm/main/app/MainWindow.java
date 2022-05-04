package com.spm.main.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.spm.main.spring.AuthHandeling;
import com.spm.main.SpotifyHdlr;
import com.spm.main.data.UserObj;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;

public class MainWindow {
	/**
	 * Open the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	static Composite leftBar;
	static Boolean iso = false;
	static UserObj user;
	static Label lblUserName = null;

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void open(String[] args) {
		Display display = Display.getDefault();
		Shell shlSpotifyPlaylistManager = new Shell();
		shlSpotifyPlaylistManager.setBackground(new Color(30, 30, 30));
		shlSpotifyPlaylistManager.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				if (iso == true) {
					AuthHandeling.stopSpring();
				}
			}
		});
		shlSpotifyPlaylistManager.setSize(901, 647);
		shlSpotifyPlaylistManager.setText("Spotify Playlist Manager");
		shlSpotifyPlaylistManager.setLayout(new FormLayout());

		leftBar = new Composite(shlSpotifyPlaylistManager, SWT.NONE);
		FormData fd_leftBar = new FormData();
		fd_leftBar.bottom = new FormAttachment(0, 608);
		fd_leftBar.right = new FormAttachment(0, 184);
		fd_leftBar.top = new FormAttachment(0, 70);
		fd_leftBar.left = new FormAttachment(0);
		leftBar.setLayoutData(fd_leftBar);
		leftBar.setBackground(SWTResourceManager.getColor(20, 50, 20));

		Button btnFuck = new Button(leftBar, SWT.NONE);
		btnFuck.setBounds(47, 95, 75, 25);
		btnFuck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				iso = true;
				AuthHandeling auth = new AuthHandeling();
				auth.runAuthHandeling(args, SpotifyHdlr.getURI());
				// btnFuck.setVisible(false);
				btnFuck.setEnabled(false);
				btnFuck.setBackground(new Color(0, 204, 0));
			}
		});
		btnFuck.setText("Fuck");
		
		Button btnUpdatelbl = new Button(leftBar, SWT.NONE);
		
		btnUpdatelbl.setBounds(47, 144, 75, 25);
		btnUpdatelbl.setText("updateLbl");

		Composite UserProfileComp = new Composite(shlSpotifyPlaylistManager, SWT.NONE);
		UserProfileComp.setLayout(null);
		FormData fd_UserProfileComp = new FormData();
		fd_UserProfileComp.bottom = new FormAttachment(0, 71);
		fd_UserProfileComp.right = new FormAttachment(0, 184);
		fd_UserProfileComp.top = new FormAttachment(0);
		fd_UserProfileComp.left = new FormAttachment(0);
		UserProfileComp.setLayoutData(fd_UserProfileComp);
		UserProfileComp.setBackground(new Color(25, 25, 25));

		lblUserName = new Label(UserProfileComp, SWT.NONE);
		lblUserName.setBackground(new Color(25, 25, 25));
		lblUserName.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblUserName.setBounds(0, 23, 184, 21);
		lblUserName.setFont(SWTResourceManager.getFont("Yu Gothic", 12, SWT.NORMAL));
		lblUserName.setAlignment(SWT.CENTER);
		lblUserName.setText("Login");
		
		
		
		btnUpdatelbl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateGuiUser();
				
			}
		});
		
		shlSpotifyPlaylistManager.open();
		shlSpotifyPlaylistManager.layout();		
		while (!shlSpotifyPlaylistManager.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public static void isSpringOn(Boolean b) {
		iso = b;
	}
	
	public static void updateGuiUser() {
		lblUserName.setText(user.getName());
		lblUserName.getParent().layout();
		System.out.println("Debug?");
		return;
	}
	
	public static void initUser() {
		user = new UserObj();
		System.out.println(user.getName());
		return;
	}
}
