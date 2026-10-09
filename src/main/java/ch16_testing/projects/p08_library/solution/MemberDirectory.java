package ch16_testing.projects.p08_library.solution;

import java.util.Optional;

public interface MemberDirectory {

    Optional<Member> find(String id);
}
