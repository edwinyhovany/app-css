import com.ccs.monitoring.application.dto.RuleDTO;

public interface ConfigureRuleUseCase {
    RuleDTO createRule(RuleDTO ruleDTO);
    RuleDTO updateRule(Long id, RuleDTO ruleDTO);
    void deleteRule(Long id);
    RuleDTO getRule(Long id);
}
