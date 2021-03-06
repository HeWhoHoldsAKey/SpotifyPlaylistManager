package com.spm.main.app;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import com.spm.main.spring.AuthHandeling;

import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

import com.spm.main.SpotifyHdlr;
import com.spm.main.data.PlaylistObj;
import com.spm.main.data.UserObj;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

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
	static Button btnLogin;
	static Color defaultColor = new Color(30, 30, 30);
	static Color btnColor = new Color(40, 40, 40);
	static Button btnViewPlaylists;
	static int imgAndBarScale = 100;
	private static Table playlistsTable;
	static ArrayList<PlaylistObj> playlists = new ArrayList<PlaylistObj>();
	private static Table tracksTable;

	/**
	 * @wbp.parser.entryPoint
	 */
	/**
	 * @param args
	 */
	public static void open(String[] args) {
		Display display = Display.getDefault();
		Shell shlSpotifyPlaylistManager = new Shell();

		Color txtColorLight = SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);

		shlSpotifyPlaylistManager.setBackground(defaultColor);
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

		Composite UserProfileComp = new Composite(shlSpotifyPlaylistManager, SWT.NONE);
		UserProfileComp.setLayout(null);
		FormData fd_UserProfileComp = new FormData();
		fd_UserProfileComp.bottom = new FormAttachment(0, 71);
		fd_UserProfileComp.right = new FormAttachment(0, 184);
		fd_UserProfileComp.top = new FormAttachment(0);
		fd_UserProfileComp.left = new FormAttachment(0);
		UserProfileComp.setLayoutData(fd_UserProfileComp);
		UserProfileComp.setBackground(new Color(25, 25, 25));
		
		//Tell the user to login
		lblUserName = new Label(UserProfileComp, SWT.NONE);
		lblUserName.setBackground(new Color(25, 25, 25));
		lblUserName.setForeground(txtColorLight);
		lblUserName.setBounds(0, 23, 184, 21);
		lblUserName.setFont(SWTResourceManager.getFont("Yu Gothic", 12, SWT.NORMAL));
		lblUserName.setAlignment(SWT.CENTER);
		lblUserName.setText("Login Below");
		lblUserName.setVisible(false);
		
		//Creats a login button
		btnLogin = new Button(UserProfileComp, SWT.NONE);
		btnLogin.setBounds(0, 0, 184, 71);
		btnLogin.setText("Login");

		//Where all the pages are stored
		CTabFolder tabFolder = new CTabFolder(shlSpotifyPlaylistManager, SWT.NO_FOCUS);
		tabFolder.setHighlightEnabled(false);
		tabFolder.setTabHeight(0);

		//Form data for the pages
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.left = new FormAttachment(leftBar, 0);
		fd_tabFolder.bottom = new FormAttachment(leftBar, 0, SWT.BOTTOM);
		fd_tabFolder.top = new FormAttachment(0);
		fd_tabFolder.right = new FormAttachment(100);
		tabFolder.setLayoutData(fd_tabFolder);
		tabFolder.setSelectionBackground(
				Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		tabFolder.setBackground(defaultColor);
		
		//Creats a button so you can see the other pages and makes it look better
		btnViewPlaylists = new Button(leftBar, SWT.NO_FOCUS | SWT.FLAT);
		btnViewPlaylists.setBounds(0, 10, 184, 39);
		btnViewPlaylists.setText("View Playlists");
		btnViewPlaylists.setBackground(btnColor);
		btnViewPlaylists.setFocus();
		btnViewPlaylists.setForeground(txtColorLight);
		btnViewPlaylists.setEnabled(false);
		btnViewPlaylists.setVisible(false);
		
		//Setting the layout for the playlist page
		CTabItem tbtmPlaylists = new CTabItem(tabFolder, SWT.NONE);
		tbtmPlaylists.setText("Playlists");

		Composite playlistsComp = new Composite(tabFolder, SWT.NO_FOCUS | SWT.FLAT);
		tbtmPlaylists.setControl(playlistsComp);
		playlistsComp.setBackground(defaultColor);

		Composite playlistsHeadingComp = new Composite(playlistsComp, SWT.NONE);
		playlistsHeadingComp.setBounds(0, 0, 699, 37);
		playlistsHeadingComp.setBackground(defaultColor);
		
		//Little lable to tell you are on the playlist page
		Label lblUsersPlaylists = new Label(playlistsHeadingComp, SWT.NONE);
		lblUsersPlaylists.setAlignment(SWT.CENTER);
		lblUsersPlaylists.setForeground(txtColorLight);
		lblUsersPlaylists.setBounds(10, 10, 679, 15);
		lblUsersPlaylists.setText("Users Playlists");
		lblUsersPlaylists.setBackground(defaultColor);

		playlistsTable = new Table(playlistsComp, SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.NO_FOCUS);
		playlistsTable.setBounds(0, 43, 699, 561);
		playlistsTable.setBackground(new Color(50, 50, 50));
		playlistsTable.setHeaderVisible(false);
		playlistsTable.setForeground(txtColorLight);

		// So windows and swt are not that happy together so we have to make this.
		// Fantastic.
		TableColumn tblclmnErrorColumn = new TableColumn(playlistsTable, SWT.NONE);
		tblclmnErrorColumn.setWidth(0);
		tblclmnErrorColumn.setText("Error Column");
		tblclmnErrorColumn.setResizable(false);

		TableColumn tblclmnPlaylists = new TableColumn(playlistsTable, SWT.NONE);
		tblclmnPlaylists.setText("Playlists");
		tblclmnPlaylists.setWidth(678);
		
		//Main page here... why is it below i have no idea but it works
		CTabItem tbtmDefaulttblitm = new CTabItem(tabFolder, SWT.NONE);
		tbtmDefaulttblitm.setText("DefaultTBLITM");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setBackground(defaultColor);
		tbtmDefaulttblitm.setControl(composite);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setForeground(txtColorLight);
		lblNewLabel.setBackground(defaultColor);
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setBounds(0, 261, 699, 15);
		lblNewLabel.setText("HEY. LOG IN SO WE CAN GET SOME WORK DONE");

		tabFolder.setSelection(tbtmDefaulttblitm);
		
		//Same thing as playlists but for the tracks inside the playlist
		CTabItem tbtmTracksfromplaylist = new CTabItem(tabFolder, SWT.NONE);
		tbtmTracksfromplaylist.setText("TracksFromPlaylist");

		Composite playlistTracksComp = new Composite(tabFolder, SWT.NO_FOCUS);
		playlistTracksComp.setBackground(SWTResourceManager.getColor(30, 30, 30));
		tbtmTracksfromplaylist.setControl(playlistTracksComp);

		Composite playlistTracksHeadingComp = new Composite(playlistTracksComp, SWT.NONE);
		playlistTracksHeadingComp.setBackground(SWTResourceManager.getColor(30, 30, 30));
		playlistTracksHeadingComp.setBounds(0, 0, 699, 37);

		Label lblUsersPlaylistTracks = new Label(playlistTracksHeadingComp, SWT.NONE);
		lblUsersPlaylistTracks.setText("(Insert Playlist Name Here) Tracks");
		lblUsersPlaylistTracks.setForeground(SWTResourceManager.getColor(227, 227, 227));
		lblUsersPlaylistTracks.setBackground(SWTResourceManager.getColor(30, 30, 30));
		lblUsersPlaylistTracks.setAlignment(SWT.CENTER);
		lblUsersPlaylistTracks.setBounds(10, 10, 679, 15);

		tracksTable = new Table(playlistTracksComp, SWT.BORDER | SWT.FULL_SELECTION);
		tracksTable.setHeaderVisible(false);
		tracksTable.setForeground(SWTResourceManager.getColor(227, 227, 227));
		tracksTable.setBackground(SWTResourceManager.getColor(50, 50, 50));
		tracksTable.setBounds(0, 0, 699, 561);

		TableColumn tblclmnTracks = new TableColumn(tracksTable, SWT.NONE);
		tblclmnTracks.setWidth(678);
		tblclmnTracks.setText("Playlists");

		/*
		 * 
		 * This area contains listeners for everything
		 * 
		 */

		// Login Button
		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				iso = true;
				btnLogin.setEnabled(false);
				btnLogin.setBackground(new Color(0, 204, 0));
				login(args);
			}
		});

		// View Play lists
		btnViewPlaylists.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tabFolder.setSelection(tbtmPlaylists);
				buildPlaylists();
				buildPlaylistTable();
			}
		});

		// This is brain numbing but it works. Just makes the rows bigger
		playlistsTable.addListener(SWT.MeasureItem, new Listener() {
			@Override
			public void handleEvent(Event event) {
				event.height = imgAndBarScale;
			}

		});

		// See if something is double clicked in the patient table
		playlistsTable.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				TableItem[] select = playlistsTable.getSelection();
				PlaylistObj p = (PlaylistObj) select[1].getData();
				p.loadTracks();
				tabFolder.setSelection(tbtmTracksfromplaylist);
				buildTracksTable(p);
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		// Turns off spring on exit if it has not already been turned off
		shlSpotifyPlaylistManager.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				if (iso == true) {
					AuthHandeling.stopSpring();
				}
			}
		});

		// NOT IMPORTANT... ish.. A loop inside a loop... to test things. why? Just because

		shlSpotifyPlaylistManager.open();
		shlSpotifyPlaylistManager.layout();
		while (!shlSpotifyPlaylistManager.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public static void buildPlaylistTable() {
		playlistsTable.removeAll();

		for (PlaylistObj p : playlists) {
			TableItem playlist = new TableItem(playlistsTable, SWT.NONE);

			playlist.setImage(1, p.getPlaylistImg());
			playlist.setText(1, p.getPlaylistName());
			playlist.setData(p.getSelf());
			System.out.println(playlist.getData());
		}

		System.out.println(playlists.size());

	}

	// Builds the table for the tracks. Could be used for alot.
	public static void buildTracksTable(PlaylistObj p) {
		tracksTable.removeAll();

		for (Track tr : p.getTracks()) {
			TableItem track = new TableItem(tracksTable, SWT.NONE);
			track.setText(tr.getName());
		}

	}

	// It builds the table before it makes the imgs soooooo yeah. Multithreading is
	// fun
	public static void setPlaylistImg(Image p, String s) {
		TableItem[] tia = playlistsTable.getItems();

		// This single this is making me hate swt and windows god why.

		for (TableItem t : tia) {
			if (t.getText(1).equalsIgnoreCase(s)) {
				int index = playlistsTable.indexOf(t);
				String tmp = t.getText(1);
				playlistsTable.remove(index);
				TableItem nti = new TableItem(playlistsTable, SWT.NONE, index);
				nti.setImage(1, p);
				nti.setText(1, tmp);

				System.out.println(nti.getText(1));

				break;

			}
		}
	}
	
	//Pain
	static void buildPlaylists() {
		playlists.clear();
		for (PlaylistSimplified p : user.getUsersPlaylistsList()) {
			PlaylistObj po = new PlaylistObj(p, imgAndBarScale);
			po.start();
			playlists.add(po);
		}
		System.out.println(playlists.size());
	}
	
	//Self explainitory
	public static void addPlaylist(PlaylistObj p) {
		playlists.add(p);
	}
	
	//Well is it?
	public static void isSpringOn(Boolean b) {
		iso = b;
	}
	
	//Also pain
	public static void login(String[] args) {
		AuthHandeling auth = new AuthHandeling();
		auth.runAuthHandeling(args, SpotifyHdlr.getURI());
	}
	
	//Well the user logged on so now do stuff...
	public static void userLoggedIn() {
		lblUserName.setText(user.getName());
		lblUserName.getParent().layout();
		btnLogin.setVisible(false);
		lblUserName.setVisible(true);
		btnViewPlaylists.setEnabled(true);
		btnViewPlaylists.setVisible(true);
	}
	
	//Who is user?
	public static void initUser() {
		user = new UserObj();
		System.out.println(user.getName());

		// To update SWT data outside of the actual SWT class... you have to do it
		// inside the display and this is the only way it works. May use more or less
		// who knows
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				userLoggedIn();
			}
		});
	}
}
