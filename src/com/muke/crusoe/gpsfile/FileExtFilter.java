package com.muke.crusoe.gpsfile;

import java.io.File;
import java.io.FileFilter;

public class FileExtFilter implements FileFilter {
	String []extensiones;
	@Override
	public boolean accept(File f) {
		// TODO Auto-generated method stub
		for (String extension : extensiones)
	    {
	      if (f.getName().toLowerCase().endsWith(extension))
	      {
	        return true;
	      }
	    }
	    return false;		
	}
	public FileExtFilter(String []e)
	{
		extensiones = e;
	}
	public FileExtFilter(String e)
	{
		extensiones = new String[1];
		extensiones[0]=e;
	}

}
