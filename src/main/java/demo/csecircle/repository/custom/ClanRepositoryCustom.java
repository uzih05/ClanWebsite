package demo.csecircle.repository.custom;

import demo.csecircle.controller.form.ClanSearchForm;
import demo.csecircle.domain.Clan;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClanRepositoryCustom {

    List<Clan> findClansByNames(ClanSearchForm form);
}
