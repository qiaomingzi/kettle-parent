http://sourceforge.net/projects/pentaho/files/
https://github.com/maven-eclipse/maven-eclipse.github.io/tree/master/maven/org/eclipse/swt
https://github.com/pentaho

修改:
org/pentaho/di/core/attributes/metastore/EmbeddedMetaStoreTest.java
org.pentaho.di.repository.filerep.KettleFileRepositoryTestBase



org.pentaho.di.ui.trans.steps.script.ScriptDialog
org.pentaho.di.ui.trans.steps.scriptvalues_mod.ScriptValuesModDialog
 ScriptNode ScriptOrFnNode
   //jscx = ContextFactory.getGlobal().enterContext();
     jscx = ContextFactory.getGlobal().enter();