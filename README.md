ProLOV: 
======
The missing plugin for ontology Engineering

A repository to create a plugin LOV for Protégé

LOV API
======
  - LOV API Documentation: http://lov.okfn.org/dataset/lov/apidoc/ (new version) 
  - LOV Search: http://lov.okfn.org/dataset/lov/apidoc/#lov2search
  - http://lov.okfn.org/dataset/lov/api/v2/search?q={TERM}&type=class for querying {TERM} as a class.
e.g. http://lov.okfn.org/dataset/lov/api/v2/search?q=Person&type=class for "Person" as Class.
  - http://lov.okfn.org/dataset/lov/api/v2/search?q={TERM}&type=property for querying {TERM} as a property
e.g.: http://lov.okfn.org/dataset/lov/api/v2/search?q=Person&type=property

Protégé Plugin development 
===========================
  - https://github.com/protegeproject/protege/wiki/Building-from-Source (v5)
  - http://protegewiki.stanford.edu/wiki/CompileProtege5InEclipseWithMaven  
  - http://protegewiki.stanford.edu/wiki/PluginAnatomy

Link to Protégé Wiki
=================
  - http://protegewiki.stanford.edu/wiki/Prot%C3%A9g%C3%A9LOV
  
Evaluation survey
==================
   - First version for the beta version, survey  at http://tinyurl.com/surveyProtegeLOV 

How to use
============
  - See the demo video at https://www.youtube.com/watch?v=UgA17N5FNzA. 
  
  

Todos List
=================
  
  - [x] Make available the plugin into Protege update mechanism
  - [ ] Implement G2 & G3 UCs 
  - [ ] Prepare a good protocol for users evaluation
  - [ ] Integrate with other vocabulary repositories: BioPortal, Ontology Design Patterns? (suggested by #reviewer 2  of ESWC paper)
  - [ ] Add ObjectProperties skos:ExactMatch, skos:narrowMatch, skos:broadMatch, skos:closeMatch. these might be useful to have in addition to the subclass and equivalence relations.

