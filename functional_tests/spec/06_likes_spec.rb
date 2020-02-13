 feature 'Creating and viewing likes' do

    scenario 'User can like a post and see the like' do
        visit '/'
        fill_in "name", with: 'user'
        fill_in "email", with: "email@me.com"
        fill_in "password", with: "password123"
        click_button('Sign up')

        visit('/posts')
        fill_in "post", with: 'This is the first test post'
        click_button('Post')
        click_button("post-id-for-like")
        expect(page).to have_content "1 likes"
    end

    scenario 'User can like a comment and see the like' do
        visit '/'
        fill_in "login-email", with: "email@me.com"
        fill_in "login-password", with: "password123"
        click_button('Log in')

        fill_in "comment", with: "This is a comment"
        click_button('Comment')

        click_button("comment-id-for-like")
        expect(page).to have_content "1 likes"
    end
end