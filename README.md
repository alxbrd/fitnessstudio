# FitnessStudio
*Generating effective mutation operators for search-based model-driven engineering.*

### Contents 

This repository contains three projects:
* **de.uni_ko.fitnessstudio**, a framework for generating efficient mutation operators
* **de.uni_ko.fitnessstudio.instance.cra**, an instance for applying this framework to the [CRA case](https://github.com/martin-fleck/cra-ttc2016/)
* **de.uni_ko.fitnessstudio.instance.cra.eval**, evaluation data obtained from the CRA instance


### Usage

**Prerequisite**

* The project was tested using [Eclipse Neon, Modeling Tools distribution](https://www.eclipse.org/downloads/packages/eclipse-modeling-tools/neonr), with an installed nightly build of [Henshin 1.4.0](https://www.eclipse.org/henshin/install.php).

**Generate mutation operators (CRA instance)**

* Execute the provided runner class *de.uni_ko.fitnessstudio.instance.cra.runners.UpperTierRunner*  to generate mutation operators for the CRA instance. The generation has a couple of configuration options, as can all be set directly in the class.
*  The results of the generation process (*.henshin* files and logs) will be stored to the directory *output_rules* within the project -- in doubt, please refresh the package explorer using F5.

**Generate solutions using a generated mutation operator (CRA instance)**

* Copy the *.henshin* file to be used for the mutation operator into the directory *de.uni_ko.fitnessstudio.instance.cra/transformation/fixed*, so that it is the only *.henshin* file in this directory.
* Execute the provided runner class *de.uni_ko.fitnessstudio.instance.cra.runners.LowerTierRunnerWithFixed* to generate solutions based on the provided mutation operator. Again, a couple of configuration options are available.
* The results of the generation process will be stored to the directory *output_models* within the project -- in doubt, please refresh the package explorer using F5.

**Developing a new instance of the framework**

* Copy the  **de.uni_ko.fitnessstudio.instance.cra** project and customize the included components for your needs.

