package Gui;

import DB.Database;

public class Operator {
	Database db;
	MainFrame mf = null;
	JoinFrame jf = null;

	public static void main(String[] args) {
		Operator opt = new Operator();
		opt.db = new Database();
		opt.mf = new MainFrame(opt);
		opt.jf = new JoinFrame(opt);
	}
}
