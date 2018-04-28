
/**
 * @author Erik Hahn Central place to define configuration keys to avoid typos
 * and inconsistencies.
 */
enum ConfigKeys {

  USE_CLASSIC_MODULE("UseClassicModule", "True"),
  LEXICON_PATH("LexiconPath", "data/opinionRoleLexicon.defaultRules.txt"),
  TEXT_PATH("TextPath", "data/hgcSample.raw.rand500.txt"),
  OUTPUT_PATH("OutputPath", "data/results.xml"),
  DEPENDENCY_PATH("DependencyPath", "data/hgcSample.parZu.rand500.txt"),
  NORMALIZE_DEPENDENCY_GRAPHS("NormalizeDependencyGraphs", "True"),
  CONSTITUENCY_PATH("ConstituencyPath", "data/hgcSample.tiger.rand500.xml"),
  PERSON_CHECK("PersonCheck", "False"),
  NAMED_ENTITY_PATH("NamedEntityPath", "data/hgc_tagged.txt"),
  GERMANET_DIR("GermaNetDir", "data/GermaNetV80/GN_V80_XML"),
  MORPHOLOGY_CHECK("MorphologyCheck", "True"),
  MORPHOLOGY_SENTIMENT_OFF("MorphologySentimentOff", "False"),
  MORPHOLOGY_PATH("MorphologyPath", "data/hgcSample.morphisto.rand500.txt"),
  FLEXIBLE_MWES("useFlexibleMWEs", "False"),
  USE_GRAMMAR_INDUCED_MODULE("UseGrammarInducedModule", "True"),
  USE_DEFAULT_MODAL_VERBS("UseDefaultModalVerbs", "True"),
  CUSTOM_MODAL_VERB_LEMMAS("CustomModalVerbLemmas", ""),
  GIM_TRIGGER_MODAL("GIMTriggerModal", "True"),
  GIM_TRIGGER_IMPERATIVE("GIMTriggerImperative", "True"),
  GIM_TRIGGER_FUTURE("GIMTriggerFuture", "True"),
  GIM_TRIGGER_SUBJUNCTIVE2_WUERDEB("GIMTriggerSubjunctive2Wuerden", "True"),
  FIND_SOURCES("FindSources", "True"),
  FIND_TARGETS("FindTargets", "True"),
  SUBJECTIVE_EXPRESSION_LOCATION_PATH("SubjectiveExpressionLocationPath", "data/shata14_adjudicated(noSourceTarget).xml"),
  IGNORE_LEXICON("IgnoreLexicon", "False"),
  USE_PRESET_SE_LOCATION_MODULE("UsePresetSELocationModule", "False");

  private final String key;
  private final String defaultValue;

  /**
   * @return <code>this.toString()</code>
   */
  public String toString() {
    return key;
  }

  /**
   * @param key The key as written in a configuration file and used by a
   * {@link java.util.Properties} instance.
   * @param defaultValue Default value
   */
  private ConfigKeys(String key, String defaultValue) {
    this.key = key;
    this.defaultValue = defaultValue;
  }

  /**
   * @return The key as written in a configuration file and used by a
   * {@link java.util.Properties} instance.
   */
  public String getKey() {
    return key;
  }

  /**
   * @return The default value of this variable.
   */
  public String getDefaultValue() {
    return defaultValue;
  }
}
