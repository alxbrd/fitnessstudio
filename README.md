# Fitnessstudio
*Generating effective mutation operators for search-based model-driven engineering.*

### Contents 

This repository contains three projects:
* **de.uni_ko.fitnessstudio**, a framework for generation efficient mutation operators
* **de.uni_ko.fitnessstudio.instance.cra**, an instance for applying this framework to the CRA case
* **de.uni_ko.fitnessstudio.instance.cra.eval**, evaluation data obtained from the CRA instance


### Usage

**Generate mutation operators (CRA instance)**

* Execute the provided runner class *de.uni_ko.fitnessstudio.instance.cra.runners.UpperTierRunner*  to generate mutation operators for the CRA instance. The generation has a couple of options, as can all be configured directly in the class.
*  The results of the generation process (*.henshin* files and logs) will be stored to the directory *output_rules* within the project -- in doubt, please refresh the package explorer using F5.

**Generate solutions using a generated mutation operator (CRA instance)**

* Copy the *.henshin* file to be used for the mutation operator into the directory *de.uni_ko.fitnessstudio.instance.cra/transformation/fixed*, so that it is the only *.henshin* file in this directory.
* Execute the provided runner class *de.uni_ko.fitnessstudio.instance.cra.runners.LowerTierRunnerWithFixed* to generate solutions based on the provided mutation operator.
* The results of the generation process will be stored to the directory *output_models* within the project -- in doubt, please refresh the package explorer using F5.

**Developing a new instance of the framework**

Copy the  **de.uni_ko.fitnessstudio.instance.cra** instance and cutomize the included components for your needs.

