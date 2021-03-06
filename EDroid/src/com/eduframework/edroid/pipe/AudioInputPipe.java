/*
 * Camundo <http://www.camundo.com> Copyright (C) 2011  Wouter Van der Beken.
 *
 * This file is part of Camundo.
 *
 * Camundo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Camundo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Camundo.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.eduframework.edroid.pipe;

import java.io.IOException;

public interface AudioInputPipe {
	
	public void start();
	
	public boolean initialized() throws IOException;
	
	public void writeBootstrap();
	
	
	public void write( int oneByte ) throws IOException;
	public void write( byte[] buffer, int offset, int length ) throws IOException;
	
	public void close();

}
