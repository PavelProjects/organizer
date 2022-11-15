package com.povobolapo.organizer;

import com.povobolapo.organizer.model.ContentInfoEntity;
import com.povobolapo.organizer.service.ContentService;
import com.povobolapo.organizer.service.StorageService;
import com.povobolapo.organizer.service.UserDetailsServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.io.InputStream;

@SpringBootTest(classes = OrganizerApplication.class)
public class ContentTestIT {
    @Autowired
    private ContentService contentService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private final String TEST_FILE_NAME = "test_ava.png";
    private static final String AUTOTEST_LOGIN = "autotest_user";

    @Test
    public void testStorage() throws IOException {
        String contentId = "12345678";
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(TEST_FILE_NAME)) {
            assert in != null;
            assert storageService.save(contentId, in.readAllBytes()).equals(storageService.getBasePath() + "/" + contentId);
            assert storageService.get(contentId) != null;
            assert storageService.delete("12345678");
        }
    }

    @Test
    public void testContent() throws IOException {
        setSecurityContext(AUTOTEST_LOGIN);
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(TEST_FILE_NAME)) {
            assert in != null;
            String[] splitName = StringUtils.split(TEST_FILE_NAME, ".");

            ContentInfoEntity contentInfoEntity = contentService.createContent(splitName[0], splitName[1], in.readAllBytes());
            assert contentInfoEntity != null;
            assert contentInfoEntity.getFileName().equals(splitName[0]);
            assert contentInfoEntity.getContent().getFileExtension().equals(splitName[1]);
            assert contentInfoEntity.getContent() != null;

            assert contentService.getContentInfo(contentInfoEntity.getId()) != null;

            contentService.deleteContent(contentInfoEntity.getId());
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    private void setSecurityContext(String login) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
