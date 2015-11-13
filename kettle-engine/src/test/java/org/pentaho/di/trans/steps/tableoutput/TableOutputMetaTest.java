/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2015 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.trans.steps.tableoutput;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.steps.loadsave.MemoryRepository;
import org.pentaho.metastore.api.IMetaStore;

public class TableOutputMetaTest {

  private List<DatabaseMeta> databases;
  private IMetaStore metaStore;

  @SuppressWarnings( "unchecked" )
  @Before
  public void setUp() {
    databases = mock( List.class );
    metaStore = mock( IMetaStore.class );
  }

  /**
   * @see 
   *     <a href=http://jira.pentaho.com/browse/BACKLOG-377>http://jira.pentaho.com/browse/BACKLOG-377</a>
   * @throws KettleException
   */
  @Test
  public void testReadRep() throws KettleException {

    //check variable
    String commitSize = "${test}";

    Repository rep = new MemoryRepository();
    rep.saveStepAttribute( null, null, "commit", commitSize );

    TableOutputMeta tableOutputMeta = new TableOutputMeta();
    tableOutputMeta.readRep( rep, metaStore, null, databases );

    assertEquals( commitSize, tableOutputMeta.getCommitSize() );

    //check integer size
    int commitSizeInt = 1;
    Repository rep2 = new MemoryRepository();
    rep2.saveStepAttribute( null, null, "commit", commitSizeInt );

    TableOutputMeta tableOutputMeta2 = new TableOutputMeta();
    tableOutputMeta2.readRep( rep2, metaStore, null, databases );

    assertEquals( String.valueOf( commitSizeInt ), tableOutputMeta2.getCommitSize() );
  }

  @Test
  public void testIsReturningGeneratedKeys() throws Exception {
    TableOutputMeta tableOutputMeta = new TableOutputMeta(),
        tableOutputMetaSpy = spy( tableOutputMeta );

    DatabaseMeta databaseMeta = mock( DatabaseMeta.class );
    doReturn( true ).when( databaseMeta ).supportsAutoGeneratedKeys();
    doReturn( databaseMeta ).when( tableOutputMetaSpy ).getDatabaseMeta();

    tableOutputMetaSpy.setReturningGeneratedKeys( true );
    assertTrue( tableOutputMetaSpy.isReturningGeneratedKeys() );

    doReturn( false ).when( databaseMeta ).supportsAutoGeneratedKeys();
    assertFalse( tableOutputMetaSpy.isReturningGeneratedKeys() );

    tableOutputMetaSpy.setReturningGeneratedKeys( true );
    assertFalse( tableOutputMetaSpy.isReturningGeneratedKeys() );

    tableOutputMetaSpy.setReturningGeneratedKeys( false );
    assertFalse( tableOutputMetaSpy.isReturningGeneratedKeys() );
  }

  @Test
  public void testProvidesModeler() throws Exception {
    TableOutputMeta tableOutputMeta = new TableOutputMeta();
    tableOutputMeta.setFieldDatabase( new String[] {"f1", "f2", "f3"} );
    tableOutputMeta.setFieldStream( new String[] {"s4", "s5", "s6"} );

    TableOutputData tableOutputData = new TableOutputData();
    tableOutputData.insertRowMeta = mock( RowMeta.class );
    assertEquals( tableOutputData.insertRowMeta, tableOutputMeta.getRowMeta( tableOutputData ) );

    tableOutputMeta.setSpecifyFields( false );
    assertEquals( 0, tableOutputMeta.getDatabaseFields().size() );
    assertEquals( 0, tableOutputMeta.getStreamFields().size() );

    tableOutputMeta.setSpecifyFields( true );
    assertEquals( 3, tableOutputMeta.getDatabaseFields().size() );
    assertEquals( "f1", tableOutputMeta.getDatabaseFields().get( 0 ) );
    assertEquals( "f2", tableOutputMeta.getDatabaseFields().get( 1 ) );
    assertEquals( "f3", tableOutputMeta.getDatabaseFields().get( 2 ) );
    assertEquals( 3, tableOutputMeta.getStreamFields().size() );
    assertEquals( "s4", tableOutputMeta.getStreamFields().get( 0 ) );
    assertEquals( "s5", tableOutputMeta.getStreamFields().get( 1 ) );
    assertEquals( "s6", tableOutputMeta.getStreamFields().get( 2 ) );
  }

}
