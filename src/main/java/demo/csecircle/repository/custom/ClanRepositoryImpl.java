package demo.csecircle.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import demo.csecircle.controller.form.ClanSearchForm;
import demo.csecircle.domain.Clan;
import demo.csecircle.domain.QClan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static demo.csecircle.domain.QClan.clan;

@Repository
@RequiredArgsConstructor
public class ClanRepositoryImpl implements ClanRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Clan> findClansByNames(ClanSearchForm form) {
        return queryFactory.selectFrom(clan)
                .where(clan.clanName.like("%"+form.getClanName()+"%"))
                .fetch();
    }
}
