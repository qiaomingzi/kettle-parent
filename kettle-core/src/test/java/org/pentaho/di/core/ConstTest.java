/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2013 by Pentaho : http://www.pentaho.com
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

package org.pentaho.di.core;

import java.math.BigDecimal;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

/**
 * Test class for the basic functionality of Const.
 *
 */
public class ConstTest extends TestCase {
  protected boolean isArraySorted( String[] arr ) {
    if ( arr.length < 2 ) {
      return true;
    }

    for ( int idx = 0; idx < arr.length - 1; idx++ ) {
      if ( arr[idx].compareTo( arr[idx + 1] ) > 0 ) {
        return false;
      }
    }

    return true;
  }

  /**
   * Test initCap. Regression test for PDI-1338: "javascript initcap() can't deal correctly with special non-ASCII
   * chars".
   */
  @Test
  public void testInitCap() {
    assertEquals( "Sven", Const.initCap( "Sven" ) );
    assertEquals( "Matt", Const.initCap( "MATT" ) );
    assertEquals( "Sven Boden", Const.initCap( "sven boden" ) );
    assertEquals( "Sven  Boden ", Const.initCap( "sven  boden " ) );
    assertEquals( "Sven Boden Was Here", Const.initCap( "sven boden was here" ) );

    // Here the original code failed as it saw the "o umlaut" as non-ASCII, and would
    // assume it needed to start a new word here.
    assertEquals( "K\u00F6nnen", Const.initCap( "k\u00F6nnen" ) );
  }

  /**
   * Test sortString().
   */
  @Test
  public void testSortStrings() {
    String[] arr1 = { "Red", "Blue", "Black", "Black", "Green" };
    String[] arr2 = { "aaa", "zzz", "yyy", "sss", "ttt", "t" };
    String[] arr3 = { "A", "B", "C", "D" };

    String[] results = Const.sortStrings( arr1 );
    assertTrue( isArraySorted( arr1 ) );
    assertTrue( isArraySorted( results ) );

    results = Const.sortStrings( arr2 );
    assertTrue( isArraySorted( arr2 ) );
    assertTrue( isArraySorted( results ) );

    results = Const.sortStrings( arr3 );
    assertTrue( isArraySorted( arr3 ) );
    assertTrue( isArraySorted( results ) );
  }

  @Test
  public void testIsEmpty() {
    assertEquals( true, Const.isEmpty( (String) null ) );
    assertEquals( true, Const.isEmpty( "" ) );
    assertEquals( false, Const.isEmpty( "test" ) );
  }

  @Test
  public void testIsEmptyStringBuffer() {
    assertEquals( true, Const.isEmpty( (StringBuffer) null ) );
    assertEquals( true, Const.isEmpty( new StringBuffer( "" ) ) );
    assertEquals( false, Const.isEmpty( new StringBuffer( "test" ) ) );
  }

  @Test
  public void testNVL() {
    assertNull( Const.NVL( null, null ) );
    assertEquals( "test", Const.NVL( "test", "test1" ) );
    assertEquals( "test", Const.NVL( "test", null ) );
    assertEquals( "test1", Const.NVL( null, "test1" ) );
  }

  @Test
  public void testNrSpacesBefore() {
    try {
      Const.nrSpacesBefore( null );
      fail( "Expected NullPointerException" );
    } catch ( NullPointerException ex ) {
    }

    assertEquals( 0, Const.nrSpacesBefore( "" ) );
    assertEquals( 1, Const.nrSpacesBefore( " " ) );
    assertEquals( 3, Const.nrSpacesBefore( "   " ) );
    assertEquals( 0, Const.nrSpacesBefore( "test" ) );
    assertEquals( 0, Const.nrSpacesBefore( "test  " ) );
    assertEquals( 3, Const.nrSpacesBefore( "   test" ) );
    assertEquals( 4, Const.nrSpacesBefore( "    test  " ) );
  }

  @Test
  public void testNrSpacesAfter() {
    try {
      Const.nrSpacesAfter( null );
      fail( "Expected NullPointerException" );
    } catch ( NullPointerException ex ) {
    }

    assertEquals( 0, Const.nrSpacesAfter( "" ) );
    assertEquals( 1, Const.nrSpacesAfter( " " ) );
    assertEquals( 3, Const.nrSpacesAfter( "   " ) );
    assertEquals( 0, Const.nrSpacesAfter( "test" ) );
    assertEquals( 2, Const.nrSpacesAfter( "test  " ) );
    assertEquals( 0, Const.nrSpacesAfter( "   test" ) );
    assertEquals( 2, Const.nrSpacesAfter( "    test  " ) );
  }

  @Test
  public void testLtrim() {
    assertEquals( null, Const.ltrim( null ) );
    assertEquals( "", Const.ltrim( "" ) );
    assertEquals( "", Const.ltrim( "  " ) );
    assertEquals( "test ", Const.ltrim( "test " ) );
    assertEquals( "test ", Const.ltrim( "  test " ) );
  }

  @Test
  public void testRtrim() {
    assertEquals( null, Const.rtrim( null ) );
    assertEquals( "", Const.rtrim( "" ) );
    assertEquals( "", Const.rtrim( "  " ) );
    assertEquals( "test", Const.rtrim( "test " ) );
    assertEquals( "test ", Const.ltrim( "  test " ) );
  }

  @Test
  public void testTrim() {
    assertEquals( null, Const.trim( null ) );
    assertEquals( "", Const.trim( "" ) );
    assertEquals( "", Const.trim( "  " ) );
    assertEquals( "test", Const.trim( "test " ) );
    assertEquals( "test", Const.trim( "  test " ) );
  }

  @Test
  public void testOnlySpaces() {
    try {
      Const.onlySpaces( null );
      fail( "Expected NullPointerException" );
    } catch ( NullPointerException ex ) {
    }
    assertEquals( true, Const.onlySpaces( "" ) );
    assertEquals( true, Const.onlySpaces( "  " ) );
    assertEquals( false, Const.onlySpaces( "   test " ) );
  }

  /**
   * Test splitString with String separator.
   */
  @Test
  public void testSplitString() {
    assertEquals( 0, Const.splitString( "", ";" ).length );
    assertEquals( 0, Const.splitString( null, ";" ).length );

    String[] a = Const.splitString( ";", ";" );
    assertEquals( 1, a.length );
    assertEquals( "", a[0] );

    a = Const.splitString( "a;b;c;d", ";" );
    assertEquals( 4, a.length );
    assertEquals( "a", a[0] );
    assertEquals( "b", a[1] );
    assertEquals( "c", a[2] );
    assertEquals( "d", a[3] );

    a = Const.splitString( "a;b;c;d;", ";" );
    assertEquals( 4, a.length );
    assertEquals( "a", a[0] );
    assertEquals( "b", a[1] );
    assertEquals( "c", a[2] );
    assertEquals( "d", a[3] );

    a = Const.splitString( "AACCAADAaAADD", "AA" );
    assertEquals( 4, a.length );
    assertEquals( "", a[0] );
    assertEquals( "CC", a[1] );
    assertEquals( "DA", a[2] );
    assertEquals( "ADD", a[3] );

    a = Const.splitString( "CCAABBAA", "AA" );
    assertEquals( 2, a.length );
    assertEquals( "CC", a[0] );
    assertEquals( "BB", a[1] );
  }

  /**
   * Test splitString with char separator.
   */
  @Test
  public void testSplitStringChar() {
    assertEquals( 0, Const.splitString( "", ';' ).length );
    assertEquals( 0, Const.splitString( null, ';' ).length );

    String[] a = Const.splitString( ";", ';' );
    assertEquals( 1, a.length );
    assertEquals( "", a[0] );

    a = Const.splitString( "a;b;c;d", ';' );
    assertEquals( 4, a.length );
    assertEquals( "a", a[0] );
    assertEquals( "b", a[1] );
    assertEquals( "c", a[2] );
    assertEquals( "d", a[3] );

    a = Const.splitString( "a;b;c;d;", ';' );
    assertEquals( 4, a.length );
    assertEquals( "a", a[0] );
    assertEquals( "b", a[1] );
    assertEquals( "c", a[2] );
    assertEquals( "d", a[3] );

    a = Const.splitString( ";CC;DA;ADD", ';' );
    assertEquals( 4, a.length );
    assertEquals( "", a[0] );
    assertEquals( "CC", a[1] );
    assertEquals( "DA", a[2] );
    assertEquals( "ADD", a[3] );

    a = Const.splitString( "CC;BB;", ';' );
    assertEquals( 2, a.length );
    assertEquals( "CC", a[0] );
    assertEquals( "BB", a[1] );
  }

  /**
   * Test splitString with delimiter and enclosure
   */
  @Test
  public void testSplitStringWithDelimiterAndEnclosure() {
    String[] result = null;

    result = Const.splitString( null, null, null );
    assertNull( result );

    result = Const.splitString( "Hello, world", null, null );
    assertNotNull( result );
    assertEquals( result.length, 1 );
    assertEquals( result[0], "Hello, world" );

    result = Const.splitString( "Hello, world", ",", null );
    assertNotNull( result );
    assertEquals( result.length, 2 );
    assertEquals( result[0], "Hello" );
    assertEquals( result[1], " world" );

    result = Const.splitString( "Hello, world", ",", "" );
    assertNotNull( result );
    assertEquals( result.length, 2 );
    assertEquals( result[0], "Hello" );
    assertEquals( result[1], " world" );

    result = Const.splitString( "\"Hello, world\"", ",", "\"" );
    assertNotNull( result );
    assertEquals( result.length, 1 );
    assertEquals( result[0], "\"Hello, world\"" );

    result = Const.splitString( "\"Hello, world\",\"I\",\"am\",\"here\"", ",", "\"" );
    assertNotNull( result );
    assertEquals( result.length, 4 );
    assertEquals( result[0], "\"Hello, world\"" );
    assertEquals( result[1], "\"I\"" );
    assertEquals( result[2], "\"am\"" );
    assertEquals( result[3], "\"here\"" );

    result = Const.splitString( "\"Hello, world\",\"I,\",\"am,,\",\", here\"", ",", "\"" );
    assertNotNull( result );
    assertEquals( result.length, 4 );
    assertEquals( result[0], "\"Hello, world\"" );
    assertEquals( result[1], "\"I,\"" );
    assertEquals( result[2], "\"am,,\"" );
    assertEquals( result[3], "\", here\"" );

    // Try a different delimiter and enclosure
    result = Const.splitString( "a;'b;c;d';'e,f';'g';h", ";", "'" );
    assertNotNull( result );
    assertEquals( result.length, 5 );
    assertEquals( result[0], "a" );
    assertEquals( result[1], "'b;c;d'" );
    assertEquals( result[2], "'e,f'" );
    assertEquals( result[3], "'g'" );
    assertEquals( result[4], "h" );

    // Check for null and empty as the last split
    result = Const.splitString( "a;b;c;", ";", null );
    assertNotNull( result );
    assertEquals( result.length, 3 );

    result = Const.splitString( "a;b;c;''", ";", "'" );
    assertNotNull( result );
    assertEquals( result.length, 4 );

    // Check for multiple-character strings
    result =
      Const.splitString( "html this is a web page html</newpage>html and so is this html", "</newpage>", "html" );
    assertNotNull( result );
    assertEquals( result.length, 2 );
    assertEquals( result[0], "html this is a web page html" );
    assertEquals( result[1], "html and so is this html" );
  }

  /**
   * Test splitString with delimiter and enclosure
   */
  @Test
  public void testSplitStringWithEscaping() {
    String[] result = null;

    result = Const.splitString( null, null, null );
    assertNull( result );

    result = Const.splitString( "Hello, world", null, null );
    assertNotNull( result );
    assertEquals( result.length, 1 );
    assertEquals( result[0], "Hello, world" );

    result = Const.splitString( "Hello\\, world,Hello\\, planet,Hello\\, 3rd rock", ',', true );
    assertNotNull( result );
    assertEquals( result.length, 3 );
    assertEquals( result[0], "Hello\\, world" );
    assertEquals( result[1], "Hello\\, planet" );
    assertEquals( result[2], "Hello\\, 3rd rock" );
  }

  /**
   * Test splitPath.
   */
  @Test
  public void testSplitPath() {
    String[] a = Const.splitPath( "", "/" );
    assertEquals( 0, a.length );

    a = Const.splitPath( null, "/" );
    assertEquals( 0, a.length );

    a = Const.splitPath( "/", "/" );
    assertEquals( 0, a.length );

    a = Const.splitPath( "/level1", "/" );
    assertEquals( 1, a.length );
    assertEquals( "level1", a[0] );

    a = Const.splitPath( "level1", "/" );
    assertEquals( 1, a.length );
    assertEquals( "level1", a[0] );

    a = Const.splitPath( "/level1/level2", "/" );
    assertEquals( 2, a.length );
    assertEquals( "level1", a[0] );
    assertEquals( "level2", a[1] );

    a = Const.splitPath( "level1/level2", "/" );
    assertEquals( 2, a.length );
    assertEquals( "level1", a[0] );
    assertEquals( "level2", a[1] );

    a = Const.splitPath( "/level1/level2/lvl3", "/" );
    assertEquals( 3, a.length );
    assertEquals( "level1", a[0] );
    assertEquals( "level2", a[1] );
    assertEquals( "lvl3", a[2] );

    a = Const.splitPath( "level1/level2/lvl3", "/" );
    assertEquals( 3, a.length );
    assertEquals( "level1", a[0] );
    assertEquals( "level2", a[1] );
    assertEquals( "lvl3", a[2] );
  }

  @Test
  public void testRound_BigDecimal() {
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.0" ), 0, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.0" ), 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.0" ), 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.0" ), 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.0" ), 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.0" ), 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.0" ), 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.0" ), 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "1.2" ), 0, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.2" ), 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "1.2" ), 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.2" ), 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.2" ), 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.2" ), 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.2" ), 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.2" ), 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "1.5" ), 0, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.5" ), 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "1.5" ), 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.5" ), 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "1.5" ), 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.5" ), 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "1.5" ), 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "1.5" ), 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "1.7" ), 0, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.7" ), 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "1.7" ), 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "1.0" ), Const.round( new BigDecimal( "1.7" ), 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "1.7" ), 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "1.7" ), 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "1.7" ), 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "1.7" ), 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.0" ), 0, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.0" ), 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.0" ), 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.0" ), 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.0" ), 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.0" ), 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.0" ), 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.0" ), 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "3.0" ), Const.round( new BigDecimal( "2.2" ), 0, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.2" ), 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "3.0" ), Const.round( new BigDecimal( "2.2" ), 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.2" ), 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.2" ), 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.2" ), 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.2" ), 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.2" ), 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "3.0" ), Const.round( new BigDecimal( "2.5" ), 0, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.5" ), 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "3.0" ), Const.round( new BigDecimal( "2.5" ), 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.5" ), 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "3.0" ), Const.round( new BigDecimal( "2.5" ), 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.5" ), 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.5" ), 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "3.0" ), Const.round( new BigDecimal( "2.5" ), 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "3.0" ), Const.round( new BigDecimal( "2.7" ), 0, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.7" ), 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "3.0" ), Const.round( new BigDecimal( "2.7" ), 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "2.0" ), Const.round( new BigDecimal( "2.7" ), 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "3.0" ), Const.round( new BigDecimal( "2.7" ), 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "3.0" ), Const.round( new BigDecimal( "2.7" ), 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "3.0" ), Const.round( new BigDecimal( "2.7" ), 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "3.0" ), Const.round( new BigDecimal( "2.7" ), 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.0" ), 0, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.0" ), 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.0" ), 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.0" ), 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.0" ), 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.0" ), 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.0" ), 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.0" ), 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-1.2" ), 0, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.2" ), 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.2" ), 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-1.2" ), 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.2" ), 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.2" ), 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.2" ), 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.2" ), 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-1.5" ), 0, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.5" ), 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.5" ), 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-1.5" ), 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-1.5" ), 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.5" ), 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-1.5" ), 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.5" ), 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-1.7" ), 0, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.7" ), 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-1.0" ), Const.round( new BigDecimal( "-1.7" ), 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-1.7" ), 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-1.7" ), 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-1.7" ), 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-1.7" ), 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-1.7" ), 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.0" ), 0, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.0" ), 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.0" ), 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.0" ), 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.0" ), 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.0" ), 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.0" ), 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.0" ), 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-3.0" ), Const.round( new BigDecimal( "-2.2" ), 0, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.2" ), 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.2" ), 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-3.0" ), Const.round( new BigDecimal( "-2.2" ), 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.2" ), 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.2" ), 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.2" ), 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.2" ), 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-3.0" ), Const.round( new BigDecimal( "-2.5" ), 0, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.5" ), 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.5" ), 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-3.0" ), Const.round( new BigDecimal( "-2.5" ), 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-3.0" ), Const.round( new BigDecimal( "-2.5" ), 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.5" ), 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.5" ), 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.5" ), 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-3.0" ), Const.round( new BigDecimal( "-2.7" ), 0, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.7" ), 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-2.0" ), Const.round( new BigDecimal( "-2.7" ), 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-3.0" ), Const.round( new BigDecimal( "-2.7" ), 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-3.0" ), Const.round( new BigDecimal( "-2.7" ), 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-3.0" ), Const.round( new BigDecimal( "-2.7" ), 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-3.0" ), Const.round( new BigDecimal( "-2.7" ), 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-3.0" ), Const.round( new BigDecimal( "-2.7" ), 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.010" ), 2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.010" ), 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.010" ), 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.010" ), 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.010" ), 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.010" ), 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.010" ), 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.010" ), 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.012" ), 2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.012" ), 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.012" ), 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.012" ), 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.012" ), 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.012" ), 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.012" ), 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.012" ), 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.015" ), 2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.015" ), 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.015" ), 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.015" ), 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.015" ), 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.015" ), 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.015" ), 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.015" ), 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.017" ), 2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.017" ), 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.017" ), 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "0.010" ), Const.round( new BigDecimal( "0.017" ), 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.017" ), 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.017" ), 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.017" ), 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.017" ), 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.020" ), 2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.020" ), 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.020" ), 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.020" ), 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.020" ), 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.020" ), 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.020" ), 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.020" ), 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "0.030" ), Const.round( new BigDecimal( "0.022" ), 2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.022" ), 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "0.030" ), Const.round( new BigDecimal( "0.022" ), 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.022" ), 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.022" ), 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.022" ), 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.022" ), 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.022" ), 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "0.030" ), Const.round( new BigDecimal( "0.025" ), 2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.025" ), 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "0.030" ), Const.round( new BigDecimal( "0.025" ), 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.025" ), 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "0.030" ), Const.round( new BigDecimal( "0.025" ), 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.025" ), 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.025" ), 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "0.030" ), Const.round( new BigDecimal( "0.025" ), 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "0.030" ), Const.round( new BigDecimal( "0.027" ), 2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.027" ), 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "0.030" ), Const.round( new BigDecimal( "0.027" ), 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "0.020" ), Const.round( new BigDecimal( "0.027" ), 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "0.030" ), Const.round( new BigDecimal( "0.027" ), 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "0.030" ), Const.round( new BigDecimal( "0.027" ), 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "0.030" ), Const.round( new BigDecimal( "0.027" ), 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "0.030" ), Const.round( new BigDecimal( "0.027" ), 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.010" ), 2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.010" ), 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.010" ), 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.010" ), 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.010" ), 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.010" ), 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.010" ), 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.010" ), 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.012" ), 2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.012" ), 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.012" ), 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.012" ), 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.012" ), 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.012" ), 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.012" ), 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.012" ), 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.015" ), 2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.015" ), 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.015" ), 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.015" ), 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.015" ), 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.015" ), 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.015" ), 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.015" ), 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.017" ), 2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.017" ), 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-0.010" ), Const.round( new BigDecimal( "-0.017" ), 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.017" ), 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.017" ), 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.017" ), 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.017" ), 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.017" ), 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.020" ), 2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.020" ), 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.020" ), 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.020" ), 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.020" ), 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.020" ), 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.020" ), 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.020" ), 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-0.030" ), Const.round( new BigDecimal( "-0.022" ), 2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.022" ), 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.022" ), 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-0.030" ), Const.round( new BigDecimal( "-0.022" ), 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.022" ), 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.022" ), 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.022" ), 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.022" ), 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-0.030" ), Const.round( new BigDecimal( "-0.025" ), 2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.025" ), 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.025" ), 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-0.030" ), Const.round( new BigDecimal( "-0.025" ), 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-0.030" ), Const.round( new BigDecimal( "-0.025" ), 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.025" ), 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.025" ), 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.025" ), 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-0.030" ), Const.round( new BigDecimal( "-0.027" ), 2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.027" ), 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-0.020" ), Const.round( new BigDecimal( "-0.027" ), 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-0.030" ), Const.round( new BigDecimal( "-0.027" ), 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-0.030" ), Const.round( new BigDecimal( "-0.027" ), 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-0.030" ), Const.round( new BigDecimal( "-0.027" ), 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-0.030" ), Const.round( new BigDecimal( "-0.027" ), 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-0.030" ), Const.round( new BigDecimal( "-0.027" ), 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "100.0" ), -2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "100.0" ), -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "100.0" ), -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "100.0" ), -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "100.0" ), -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "100.0" ), -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "100.0" ), -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "100.0" ), -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "120.0" ), -2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "120.0" ), -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "120.0" ), -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "120.0" ), -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "120.0" ), -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "120.0" ), -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "120.0" ), -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "120.0" ), -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "150.0" ), -2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "150.0" ), -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "150.0" ), -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "150.0" ), -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "150.0" ), -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "150.0" ), -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "150.0" ), -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "150.0" ), -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "170.0" ), -2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "170.0" ), -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "170.0" ), -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "100.0" ), Const.round( new BigDecimal( "170.0" ), -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "170.0" ), -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "170.0" ), -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "170.0" ), -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "170.0" ), -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "200.0" ), -2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "200.0" ), -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "200.0" ), -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "200.0" ), -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "200.0" ), -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "200.0" ), -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "200.0" ), -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "200.0" ), -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "300.0" ), Const.round( new BigDecimal( "220.0" ), -2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "220.0" ), -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "300.0" ), Const.round( new BigDecimal( "220.0" ), -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "220.0" ), -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "220.0" ), -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "220.0" ), -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "220.0" ), -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "220.0" ), -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "300.0" ), Const.round( new BigDecimal( "250.0" ), -2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "250.0" ), -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "300.0" ), Const.round( new BigDecimal( "250.0" ), -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "250.0" ), -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "300.0" ), Const.round( new BigDecimal( "250.0" ), -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "250.0" ), -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "250.0" ), -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "300.0" ), Const.round( new BigDecimal( "250.0" ), -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "300.0" ), Const.round( new BigDecimal( "270.0" ), -2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "270.0" ), -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "300.0" ), Const.round( new BigDecimal( "270.0" ), -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "200.0" ), Const.round( new BigDecimal( "270.0" ), -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "300.0" ), Const.round( new BigDecimal( "270.0" ), -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "300.0" ), Const.round( new BigDecimal( "270.0" ), -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "300.0" ), Const.round( new BigDecimal( "270.0" ), -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "300.0" ), Const.round( new BigDecimal( "270.0" ), -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-100.0" ), -2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-100.0" ), -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-100.0" ), -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-100.0" ), -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-100.0" ), -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-100.0" ), -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-100.0" ), -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-100.0" ), -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-120.0" ), -2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-120.0" ), -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-120.0" ), -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-120.0" ), -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-120.0" ), -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-120.0" ), -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-120.0" ), -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-120.0" ), -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-150.0" ), -2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-150.0" ), -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-150.0" ), -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-150.0" ), -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-150.0" ), -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-150.0" ), -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-150.0" ), -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-150.0" ), -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-170.0" ), -2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-170.0" ), -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-100.0" ), Const.round( new BigDecimal( "-170.0" ), -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-170.0" ), -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-170.0" ), -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-170.0" ), -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-170.0" ), -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-170.0" ), -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-200.0" ), -2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-200.0" ), -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-200.0" ), -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-200.0" ), -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-200.0" ), -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-200.0" ), -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-200.0" ), -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-200.0" ), -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-300.0" ), Const.round( new BigDecimal( "-220.0" ), -2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-220.0" ), -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-220.0" ), -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-300.0" ), Const.round( new BigDecimal( "-220.0" ), -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-220.0" ), -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-220.0" ), -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-220.0" ), -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-220.0" ), -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-300.0" ), Const.round( new BigDecimal( "-250.0" ), -2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-250.0" ), -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-250.0" ), -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-300.0" ), Const.round( new BigDecimal( "-250.0" ), -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-300.0" ), Const.round( new BigDecimal( "-250.0" ), -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-250.0" ), -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-250.0" ), -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-250.0" ), -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( new BigDecimal( "-300.0" ), Const.round( new BigDecimal( "-270.0" ), -2, BigDecimal.ROUND_UP ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-270.0" ), -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( new BigDecimal( "-200.0" ), Const.round( new BigDecimal( "-270.0" ), -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( new BigDecimal( "-300.0" ), Const.round( new BigDecimal( "-270.0" ), -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( new BigDecimal( "-300.0" ), Const.round( new BigDecimal( "-270.0" ), -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( new BigDecimal( "-300.0" ), Const.round( new BigDecimal( "-270.0" ), -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( new BigDecimal( "-300.0" ), Const.round( new BigDecimal( "-270.0" ), -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( new BigDecimal( "-300.0" ), Const.round( new BigDecimal( "-270.0" ), -2, Const.ROUND_HALF_CEILING ) );
  }

  @Test
  public void testRound() {
    assertEquals( 1.0, Const.round( 1.0, 0, BigDecimal.ROUND_UP ) );
    assertEquals( 1.0, Const.round( 1.0, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( 1.0, Const.round( 1.0, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( 1.0, Const.round( 1.0, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 1.0, Const.round( 1.0, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 1.0, Const.round( 1.0, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 1.0, Const.round( 1.0, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 1.0, Const.round( 1.0, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( 2.0, Const.round( 1.2, 0, BigDecimal.ROUND_UP ) );
    assertEquals( 1.0, Const.round( 1.2, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( 2.0, Const.round( 1.2, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( 1.0, Const.round( 1.2, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 1.0, Const.round( 1.2, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 1.0, Const.round( 1.2, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 1.0, Const.round( 1.2, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 1.0, Const.round( 1.2, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( 2.0, Const.round( 1.5, 0, BigDecimal.ROUND_UP ) );
    assertEquals( 1.0, Const.round( 1.5, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( 2.0, Const.round( 1.5, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( 1.0, Const.round( 1.5, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 2.0, Const.round( 1.5, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 1.0, Const.round( 1.5, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 2.0, Const.round( 1.5, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 2.0, Const.round( 1.5, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( 2.0, Const.round( 1.7, 0, BigDecimal.ROUND_UP ) );
    assertEquals( 1.0, Const.round( 1.7, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( 2.0, Const.round( 1.7, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( 1.0, Const.round( 1.7, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 2.0, Const.round( 1.7, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 2.0, Const.round( 1.7, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 2.0, Const.round( 1.7, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 2.0, Const.round( 1.7, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( 2.0, Const.round( 2.0, 0, BigDecimal.ROUND_UP ) );
    assertEquals( 2.0, Const.round( 2.0, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( 2.0, Const.round( 2.0, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( 2.0, Const.round( 2.0, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 2.0, Const.round( 2.0, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 2.0, Const.round( 2.0, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 2.0, Const.round( 2.0, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 2.0, Const.round( 2.0, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( 3.0, Const.round( 2.2, 0, BigDecimal.ROUND_UP ) );
    assertEquals( 2.0, Const.round( 2.2, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( 3.0, Const.round( 2.2, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( 2.0, Const.round( 2.2, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 2.0, Const.round( 2.2, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 2.0, Const.round( 2.2, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 2.0, Const.round( 2.2, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 2.0, Const.round( 2.2, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( 3.0, Const.round( 2.5, 0, BigDecimal.ROUND_UP ) );
    assertEquals( 2.0, Const.round( 2.5, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( 3.0, Const.round( 2.5, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( 2.0, Const.round( 2.5, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 3.0, Const.round( 2.5, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 2.0, Const.round( 2.5, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 2.0, Const.round( 2.5, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 3.0, Const.round( 2.5, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( 3.0, Const.round( 2.7, 0, BigDecimal.ROUND_UP ) );
    assertEquals( 2.0, Const.round( 2.7, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( 3.0, Const.round( 2.7, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( 2.0, Const.round( 2.7, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 3.0, Const.round( 2.7, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 3.0, Const.round( 2.7, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 3.0, Const.round( 2.7, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 3.0, Const.round( 2.7, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( -1.0, Const.round( -1.0, 0, BigDecimal.ROUND_UP ) );
    assertEquals( -1.0, Const.round( -1.0, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( -1.0, Const.round( -1.0, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( -1.0, Const.round( -1.0, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -1.0, Const.round( -1.0, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -1.0, Const.round( -1.0, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -1.0, Const.round( -1.0, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -1.0, Const.round( -1.0, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( -2.0, Const.round( -1.2, 0, BigDecimal.ROUND_UP ) );
    assertEquals( -1.0, Const.round( -1.2, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( -1.0, Const.round( -1.2, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( -2.0, Const.round( -1.2, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -1.0, Const.round( -1.2, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -1.0, Const.round( -1.2, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -1.0, Const.round( -1.2, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -1.0, Const.round( -1.2, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( -2.0, Const.round( -1.5, 0, BigDecimal.ROUND_UP ) );
    assertEquals( -1.0, Const.round( -1.5, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( -1.0, Const.round( -1.5, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( -2.0, Const.round( -1.5, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -2.0, Const.round( -1.5, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -1.0, Const.round( -1.5, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -2.0, Const.round( -1.5, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -1.0, Const.round( -1.5, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( -2.0, Const.round( -1.7, 0, BigDecimal.ROUND_UP ) );
    assertEquals( -1.0, Const.round( -1.7, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( -1.0, Const.round( -1.7, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( -2.0, Const.round( -1.7, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -2.0, Const.round( -1.7, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -2.0, Const.round( -1.7, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -2.0, Const.round( -1.7, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -2.0, Const.round( -1.7, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( -2.0, Const.round( -2.0, 0, BigDecimal.ROUND_UP ) );
    assertEquals( -2.0, Const.round( -2.0, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( -2.0, Const.round( -2.0, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( -2.0, Const.round( -2.0, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -2.0, Const.round( -2.0, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -2.0, Const.round( -2.0, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -2.0, Const.round( -2.0, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -2.0, Const.round( -2.0, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( -3.0, Const.round( -2.2, 0, BigDecimal.ROUND_UP ) );
    assertEquals( -2.0, Const.round( -2.2, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( -2.0, Const.round( -2.2, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( -3.0, Const.round( -2.2, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -2.0, Const.round( -2.2, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -2.0, Const.round( -2.2, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -2.0, Const.round( -2.2, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -2.0, Const.round( -2.2, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( -3.0, Const.round( -2.5, 0, BigDecimal.ROUND_UP ) );
    assertEquals( -2.0, Const.round( -2.5, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( -2.0, Const.round( -2.5, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( -3.0, Const.round( -2.5, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -3.0, Const.round( -2.5, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -2.0, Const.round( -2.5, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -2.0, Const.round( -2.5, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -2.0, Const.round( -2.5, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( -3.0, Const.round( -2.7, 0, BigDecimal.ROUND_UP ) );
    assertEquals( -2.0, Const.round( -2.7, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( -2.0, Const.round( -2.7, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( -3.0, Const.round( -2.7, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -3.0, Const.round( -2.7, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -3.0, Const.round( -2.7, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -3.0, Const.round( -2.7, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -3.0, Const.round( -2.7, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( 0.010, Const.round( 0.010, 2, BigDecimal.ROUND_UP ) );
    assertEquals( 0.010, Const.round( 0.010, 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 0.010, Const.round( 0.010, 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 0.010, Const.round( 0.010, 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 0.010, Const.round( 0.010, 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 0.010, Const.round( 0.010, 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 0.010, Const.round( 0.010, 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 0.010, Const.round( 0.010, 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 0.020, Const.round( 0.012, 2, BigDecimal.ROUND_UP ) );
    assertEquals( 0.010, Const.round( 0.012, 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 0.020, Const.round( 0.012, 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 0.010, Const.round( 0.012, 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 0.010, Const.round( 0.012, 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 0.010, Const.round( 0.012, 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 0.010, Const.round( 0.012, 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 0.010, Const.round( 0.012, 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 0.020, Const.round( 0.015, 2, BigDecimal.ROUND_UP ) );
    assertEquals( 0.010, Const.round( 0.015, 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 0.020, Const.round( 0.015, 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 0.010, Const.round( 0.015, 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 0.020, Const.round( 0.015, 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 0.010, Const.round( 0.015, 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 0.020, Const.round( 0.015, 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 0.020, Const.round( 0.015, 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 0.020, Const.round( 0.017, 2, BigDecimal.ROUND_UP ) );
    assertEquals( 0.010, Const.round( 0.017, 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 0.020, Const.round( 0.017, 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 0.010, Const.round( 0.017, 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 0.020, Const.round( 0.017, 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 0.020, Const.round( 0.017, 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 0.020, Const.round( 0.017, 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 0.020, Const.round( 0.017, 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 0.020, Const.round( 0.020, 2, BigDecimal.ROUND_UP ) );
    assertEquals( 0.020, Const.round( 0.020, 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 0.020, Const.round( 0.020, 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 0.020, Const.round( 0.020, 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 0.020, Const.round( 0.020, 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 0.020, Const.round( 0.020, 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 0.020, Const.round( 0.020, 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 0.020, Const.round( 0.020, 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 0.030, Const.round( 0.022, 2, BigDecimal.ROUND_UP ) );
    assertEquals( 0.020, Const.round( 0.022, 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 0.030, Const.round( 0.022, 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 0.020, Const.round( 0.022, 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 0.020, Const.round( 0.022, 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 0.020, Const.round( 0.022, 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 0.020, Const.round( 0.022, 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 0.020, Const.round( 0.022, 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 0.030, Const.round( 0.025, 2, BigDecimal.ROUND_UP ) );
    assertEquals( 0.020, Const.round( 0.025, 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 0.030, Const.round( 0.025, 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 0.020, Const.round( 0.025, 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 0.030, Const.round( 0.025, 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 0.020, Const.round( 0.025, 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 0.020, Const.round( 0.025, 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 0.030, Const.round( 0.025, 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 0.030, Const.round( 0.027, 2, BigDecimal.ROUND_UP ) );
    assertEquals( 0.020, Const.round( 0.027, 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 0.030, Const.round( 0.027, 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 0.020, Const.round( 0.027, 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 0.030, Const.round( 0.027, 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 0.030, Const.round( 0.027, 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 0.030, Const.round( 0.027, 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 0.030, Const.round( 0.027, 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -0.010, Const.round( -0.010, 2, BigDecimal.ROUND_UP ) );
    assertEquals( -0.010, Const.round( -0.010, 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -0.010, Const.round( -0.010, 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -0.010, Const.round( -0.010, 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -0.010, Const.round( -0.010, 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -0.010, Const.round( -0.010, 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -0.010, Const.round( -0.010, 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -0.010, Const.round( -0.010, 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -0.020, Const.round( -0.012, 2, BigDecimal.ROUND_UP ) );
    assertEquals( -0.010, Const.round( -0.012, 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -0.010, Const.round( -0.012, 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -0.020, Const.round( -0.012, 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -0.010, Const.round( -0.012, 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -0.010, Const.round( -0.012, 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -0.010, Const.round( -0.012, 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -0.010, Const.round( -0.012, 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -0.020, Const.round( -0.015, 2, BigDecimal.ROUND_UP ) );
    assertEquals( -0.010, Const.round( -0.015, 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -0.010, Const.round( -0.015, 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -0.020, Const.round( -0.015, 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -0.020, Const.round( -0.015, 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -0.010, Const.round( -0.015, 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -0.020, Const.round( -0.015, 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -0.010, Const.round( -0.015, 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -0.020, Const.round( -0.017, 2, BigDecimal.ROUND_UP ) );
    assertEquals( -0.010, Const.round( -0.017, 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -0.010, Const.round( -0.017, 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -0.020, Const.round( -0.017, 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -0.020, Const.round( -0.017, 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -0.020, Const.round( -0.017, 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -0.020, Const.round( -0.017, 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -0.020, Const.round( -0.017, 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -0.020, Const.round( -0.020, 2, BigDecimal.ROUND_UP ) );
    assertEquals( -0.020, Const.round( -0.020, 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -0.020, Const.round( -0.020, 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -0.020, Const.round( -0.020, 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -0.020, Const.round( -0.020, 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -0.020, Const.round( -0.020, 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -0.020, Const.round( -0.020, 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -0.020, Const.round( -0.020, 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -0.030, Const.round( -0.022, 2, BigDecimal.ROUND_UP ) );
    assertEquals( -0.020, Const.round( -0.022, 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -0.020, Const.round( -0.022, 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -0.030, Const.round( -0.022, 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -0.020, Const.round( -0.022, 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -0.020, Const.round( -0.022, 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -0.020, Const.round( -0.022, 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -0.020, Const.round( -0.022, 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -0.030, Const.round( -0.025, 2, BigDecimal.ROUND_UP ) );
    assertEquals( -0.020, Const.round( -0.025, 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -0.020, Const.round( -0.025, 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -0.030, Const.round( -0.025, 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -0.030, Const.round( -0.025, 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -0.020, Const.round( -0.025, 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -0.020, Const.round( -0.025, 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -0.020, Const.round( -0.025, 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -0.030, Const.round( -0.027, 2, BigDecimal.ROUND_UP ) );
    assertEquals( -0.020, Const.round( -0.027, 2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -0.020, Const.round( -0.027, 2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -0.030, Const.round( -0.027, 2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -0.030, Const.round( -0.027, 2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -0.030, Const.round( -0.027, 2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -0.030, Const.round( -0.027, 2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -0.030, Const.round( -0.027, 2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 100.0, Const.round( 100.0, -2, BigDecimal.ROUND_UP ) );
    assertEquals( 100.0, Const.round( 100.0, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 100.0, Const.round( 100.0, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 100.0, Const.round( 100.0, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 100.0, Const.round( 100.0, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 100.0, Const.round( 100.0, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 100.0, Const.round( 100.0, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 100.0, Const.round( 100.0, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 200.0, Const.round( 120.0, -2, BigDecimal.ROUND_UP ) );
    assertEquals( 100.0, Const.round( 120.0, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 200.0, Const.round( 120.0, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 100.0, Const.round( 120.0, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 100.0, Const.round( 120.0, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 100.0, Const.round( 120.0, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 100.0, Const.round( 120.0, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 100.0, Const.round( 120.0, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 200.0, Const.round( 150.0, -2, BigDecimal.ROUND_UP ) );
    assertEquals( 100.0, Const.round( 150.0, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 200.0, Const.round( 150.0, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 100.0, Const.round( 150.0, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 200.0, Const.round( 150.0, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 100.0, Const.round( 150.0, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 200.0, Const.round( 150.0, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 200.0, Const.round( 150.0, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 200.0, Const.round( 170.0, -2, BigDecimal.ROUND_UP ) );
    assertEquals( 100.0, Const.round( 170.0, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 200.0, Const.round( 170.0, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 100.0, Const.round( 170.0, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 200.0, Const.round( 170.0, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 200.0, Const.round( 170.0, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 200.0, Const.round( 170.0, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 200.0, Const.round( 170.0, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 200.0, Const.round( 200.0, -2, BigDecimal.ROUND_UP ) );
    assertEquals( 200.0, Const.round( 200.0, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 200.0, Const.round( 200.0, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 200.0, Const.round( 200.0, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 200.0, Const.round( 200.0, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 200.0, Const.round( 200.0, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 200.0, Const.round( 200.0, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 200.0, Const.round( 200.0, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 300.0, Const.round( 220.0, -2, BigDecimal.ROUND_UP ) );
    assertEquals( 200.0, Const.round( 220.0, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 300.0, Const.round( 220.0, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 200.0, Const.round( 220.0, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 200.0, Const.round( 220.0, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 200.0, Const.round( 220.0, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 200.0, Const.round( 220.0, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 200.0, Const.round( 220.0, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 300.0, Const.round( 250.0, -2, BigDecimal.ROUND_UP ) );
    assertEquals( 200.0, Const.round( 250.0, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 300.0, Const.round( 250.0, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 200.0, Const.round( 250.0, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 300.0, Const.round( 250.0, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 200.0, Const.round( 250.0, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 200.0, Const.round( 250.0, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 300.0, Const.round( 250.0, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 300.0, Const.round( 270.0, -2, BigDecimal.ROUND_UP ) );
    assertEquals( 200.0, Const.round( 270.0, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 300.0, Const.round( 270.0, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 200.0, Const.round( 270.0, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 300.0, Const.round( 270.0, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 300.0, Const.round( 270.0, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 300.0, Const.round( 270.0, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 300.0, Const.round( 270.0, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -100.0, Const.round( -100.0, -2, BigDecimal.ROUND_UP ) );
    assertEquals( -100.0, Const.round( -100.0, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -100.0, Const.round( -100.0, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -100.0, Const.round( -100.0, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -100.0, Const.round( -100.0, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -100.0, Const.round( -100.0, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -100.0, Const.round( -100.0, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -100.0, Const.round( -100.0, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -200.0, Const.round( -120.0, -2, BigDecimal.ROUND_UP ) );
    assertEquals( -100.0, Const.round( -120.0, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -100.0, Const.round( -120.0, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -200.0, Const.round( -120.0, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -100.0, Const.round( -120.0, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -100.0, Const.round( -120.0, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -100.0, Const.round( -120.0, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -100.0, Const.round( -120.0, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -200.0, Const.round( -150.0, -2, BigDecimal.ROUND_UP ) );
    assertEquals( -100.0, Const.round( -150.0, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -100.0, Const.round( -150.0, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -200.0, Const.round( -150.0, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -200.0, Const.round( -150.0, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -100.0, Const.round( -150.0, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -200.0, Const.round( -150.0, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -100.0, Const.round( -150.0, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -200.0, Const.round( -170.0, -2, BigDecimal.ROUND_UP ) );
    assertEquals( -100.0, Const.round( -170.0, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -100.0, Const.round( -170.0, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -200.0, Const.round( -170.0, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -200.0, Const.round( -170.0, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -200.0, Const.round( -170.0, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -200.0, Const.round( -170.0, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -200.0, Const.round( -170.0, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -200.0, Const.round( -200.0, -2, BigDecimal.ROUND_UP ) );
    assertEquals( -200.0, Const.round( -200.0, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -200.0, Const.round( -200.0, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -200.0, Const.round( -200.0, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -200.0, Const.round( -200.0, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -200.0, Const.round( -200.0, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -200.0, Const.round( -200.0, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -200.0, Const.round( -200.0, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -300.0, Const.round( -220.0, -2, BigDecimal.ROUND_UP ) );
    assertEquals( -200.0, Const.round( -220.0, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -200.0, Const.round( -220.0, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -300.0, Const.round( -220.0, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -200.0, Const.round( -220.0, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -200.0, Const.round( -220.0, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -200.0, Const.round( -220.0, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -200.0, Const.round( -220.0, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -300.0, Const.round( -250.0, -2, BigDecimal.ROUND_UP ) );
    assertEquals( -200.0, Const.round( -250.0, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -200.0, Const.round( -250.0, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -300.0, Const.round( -250.0, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -300.0, Const.round( -250.0, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -200.0, Const.round( -250.0, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -200.0, Const.round( -250.0, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -200.0, Const.round( -250.0, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -300.0, Const.round( -270.0, -2, BigDecimal.ROUND_UP ) );
    assertEquals( -200.0, Const.round( -270.0, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -200.0, Const.round( -270.0, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -300.0, Const.round( -270.0, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -300.0, Const.round( -270.0, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -300.0, Const.round( -270.0, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -300.0, Const.round( -270.0, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -300.0, Const.round( -270.0, -2, Const.ROUND_HALF_CEILING ) );
  }

  @Test
  public void testRound_Long() {
    assertEquals( 1L, Const.round( 1L, 0, BigDecimal.ROUND_UP ) );
    assertEquals( 1L, Const.round( 1L, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( 1L, Const.round( 1L, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( 1L, Const.round( 1L, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 1L, Const.round( 1L, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 1L, Const.round( 1L, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 1L, Const.round( 1L, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 1L, Const.round( 1L, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( 2L, Const.round( 2L, 0, BigDecimal.ROUND_UP ) );
    assertEquals( 2L, Const.round( 2L, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( 2L, Const.round( 2L, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( 2L, Const.round( 2L, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 2L, Const.round( 2L, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 2L, Const.round( 2L, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 2L, Const.round( 2L, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 2L, Const.round( 2L, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( -1L, Const.round( -1L, 0, BigDecimal.ROUND_UP ) );
    assertEquals( -1L, Const.round( -1L, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( -1L, Const.round( -1L, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( -1L, Const.round( -1L, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -1L, Const.round( -1L, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -1L, Const.round( -1L, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -1L, Const.round( -1L, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -1L, Const.round( -1L, 0, Const.ROUND_HALF_CEILING ) );


    assertEquals( -2L, Const.round( -2L, 0, BigDecimal.ROUND_UP ) );
    assertEquals( -2L, Const.round( -2L, 0, BigDecimal.ROUND_DOWN ) );
    assertEquals( -2L, Const.round( -2L, 0, BigDecimal.ROUND_CEILING ) );
    assertEquals( -2L, Const.round( -2L, 0, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -2L, Const.round( -2L, 0, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -2L, Const.round( -2L, 0, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -2L, Const.round( -2L, 0, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -2L, Const.round( -2L, 0, Const.ROUND_HALF_CEILING ) );

    assertEquals( 100L, Const.round( 100L, -2, BigDecimal.ROUND_UP ) );
    assertEquals( 100L, Const.round( 100L, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 100L, Const.round( 100L, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 100L, Const.round( 100L, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 100L, Const.round( 100L, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 100L, Const.round( 100L, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 100L, Const.round( 100L, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 100L, Const.round( 100L, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 200L, Const.round( 120L, -2, BigDecimal.ROUND_UP ) );
    assertEquals( 100L, Const.round( 120L, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 200L, Const.round( 120L, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 100L, Const.round( 120L, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 100L, Const.round( 120L, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 100L, Const.round( 120L, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 100L, Const.round( 120L, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 100L, Const.round( 120L, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 200L, Const.round( 150L, -2, BigDecimal.ROUND_UP ) );
    assertEquals( 100L, Const.round( 150L, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 200L, Const.round( 150L, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 100L, Const.round( 150L, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 200L, Const.round( 150L, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 100L, Const.round( 150L, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 200L, Const.round( 150L, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 200L, Const.round( 150L, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 200L, Const.round( 170L, -2, BigDecimal.ROUND_UP ) );
    assertEquals( 100L, Const.round( 170L, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 200L, Const.round( 170L, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 100L, Const.round( 170L, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 200L, Const.round( 170L, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 200L, Const.round( 170L, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 200L, Const.round( 170L, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 200L, Const.round( 170L, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 200L, Const.round( 200L, -2, BigDecimal.ROUND_UP ) );
    assertEquals( 200L, Const.round( 200L, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 200L, Const.round( 200L, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 200L, Const.round( 200L, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 200L, Const.round( 200L, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 200L, Const.round( 200L, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 200L, Const.round( 200L, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 200L, Const.round( 200L, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 300L, Const.round( 220L, -2, BigDecimal.ROUND_UP ) );
    assertEquals( 200L, Const.round( 220L, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 300L, Const.round( 220L, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 200L, Const.round( 220L, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 200L, Const.round( 220L, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 200L, Const.round( 220L, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 200L, Const.round( 220L, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 200L, Const.round( 220L, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 300L, Const.round( 250L, -2, BigDecimal.ROUND_UP ) );
    assertEquals( 200L, Const.round( 250L, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 300L, Const.round( 250L, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 200L, Const.round( 250L, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 300L, Const.round( 250L, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 200L, Const.round( 250L, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 200L, Const.round( 250L, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 300L, Const.round( 250L, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( 300L, Const.round( 270L, -2, BigDecimal.ROUND_UP ) );
    assertEquals( 200L, Const.round( 270L, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( 300L, Const.round( 270L, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( 200L, Const.round( 270L, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( 300L, Const.round( 270L, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( 300L, Const.round( 270L, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( 300L, Const.round( 270L, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( 300L, Const.round( 270L, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -100L, Const.round( -100L, -2, BigDecimal.ROUND_UP ) );
    assertEquals( -100L, Const.round( -100L, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -100L, Const.round( -100L, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -100L, Const.round( -100L, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -100L, Const.round( -100L, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -100L, Const.round( -100L, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -100L, Const.round( -100L, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -100L, Const.round( -100L, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -200L, Const.round( -120L, -2, BigDecimal.ROUND_UP ) );
    assertEquals( -100L, Const.round( -120L, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -100L, Const.round( -120L, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -200L, Const.round( -120L, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -100L, Const.round( -120L, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -100L, Const.round( -120L, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -100L, Const.round( -120L, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -100L, Const.round( -120L, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -200L, Const.round( -150L, -2, BigDecimal.ROUND_UP ) );
    assertEquals( -100L, Const.round( -150L, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -100L, Const.round( -150L, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -200L, Const.round( -150L, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -200L, Const.round( -150L, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -100L, Const.round( -150L, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -200L, Const.round( -150L, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -100L, Const.round( -150L, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -200L, Const.round( -170L, -2, BigDecimal.ROUND_UP ) );
    assertEquals( -100L, Const.round( -170L, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -100L, Const.round( -170L, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -200L, Const.round( -170L, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -200L, Const.round( -170L, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -200L, Const.round( -170L, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -200L, Const.round( -170L, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -200L, Const.round( -170L, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -200L, Const.round( -200L, -2, BigDecimal.ROUND_UP ) );
    assertEquals( -200L, Const.round( -200L, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -200L, Const.round( -200L, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -200L, Const.round( -200L, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -200L, Const.round( -200L, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -200L, Const.round( -200L, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -200L, Const.round( -200L, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -200L, Const.round( -200L, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -300L, Const.round( -220L, -2, BigDecimal.ROUND_UP ) );
    assertEquals( -200L, Const.round( -220L, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -200L, Const.round( -220L, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -300L, Const.round( -220L, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -200L, Const.round( -220L, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -200L, Const.round( -220L, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -200L, Const.round( -220L, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -200L, Const.round( -220L, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -300L, Const.round( -250L, -2, BigDecimal.ROUND_UP ) );
    assertEquals( -200L, Const.round( -250L, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -200L, Const.round( -250L, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -300L, Const.round( -250L, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -300L, Const.round( -250L, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -200L, Const.round( -250L, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -200L, Const.round( -250L, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -200L, Const.round( -250L, -2, Const.ROUND_HALF_CEILING ) );

    assertEquals( -300L, Const.round( -270L, -2, BigDecimal.ROUND_UP ) );
    assertEquals( -200L, Const.round( -270L, -2, BigDecimal.ROUND_DOWN ) );
    assertEquals( -200L, Const.round( -270L, -2, BigDecimal.ROUND_CEILING ) );
    assertEquals( -300L, Const.round( -270L, -2, BigDecimal.ROUND_FLOOR ) );
    assertEquals( -300L, Const.round( -270L, -2, BigDecimal.ROUND_HALF_UP ) );
    assertEquals( -300L, Const.round( -270L, -2, BigDecimal.ROUND_HALF_DOWN ) );
    assertEquals( -300L, Const.round( -270L, -2, BigDecimal.ROUND_HALF_EVEN ) );
    assertEquals( -300L, Const.round( -270L, -2, Const.ROUND_HALF_CEILING ) );
  }

  public static void assertEquals( Object expected, Object actual ) {
    assertEquals( "", expected, actual );
  }

  public static void assertEquals( String msg, Object expected, Object actual ) {
    if ( expected instanceof BigDecimal && actual instanceof BigDecimal ) {
      if ( ( (BigDecimal) expected ).compareTo( (BigDecimal) actual ) != 0 ) {
        Assert.assertEquals( msg, expected, actual );
      }
    } else if ( expected instanceof Number && actual instanceof Number ) {
      Assert.assertEquals( msg + " dataType(" + expected + "," + actual + ")", expected.getClass(), actual.getClass() );
      Assert.assertEquals( msg, expected, actual );
    } else {
      Assert.assertEquals( msg, expected, actual );
    }
  }
}
