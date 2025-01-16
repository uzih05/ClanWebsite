package demo.csecircle.repository;

import demo.csecircle.domain.Clan;
import demo.csecircle.repository.custom.ClanRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClanRepository extends JpaRepository<Clan, Long>, ClanRepositoryCustom {
    List<Clan> findClanByMembersId(Long memberId);

}


