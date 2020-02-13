 feature 'Deleting posts and likes' do

    scenario 'User can delete a post' do
        visit '/'
        fill_in "name", with: 'user'
        fill_in "email", with: "email@me.com"
        fill_in "password", with: "password123"
        click_button('Sign up')

        visit('/posts')
        fill_in "post", with: 'This is the first test post'
        click_button('Post')
        click_button("post-id-delete")
        expect(page).not_to have_content "This is the first test post"
    end

    scenario 'User can delete a comment' do
        visit '/'
        fill_in "login-email", with: "email@me.com"
        fill_in "login-password", with: "password123"
        click_button('Log in')

        fill_in "post", with: 'This is the first test post'
        click_button('Post')
        fill_in "comment", with: "This is a comment"
        click_button('Comment')

        click_button("comment-id-delete")

        expect(page).to have_content "This is the first test post"
        expect(page).not_to have_content "This is a comment"
    end

    scenario 'User can only delete their own post' do
            visit '/'
            fill_in "name", with: 'User 2'
            fill_in "email", with: "My_email@me.com"
            fill_in "password", with: "password123"
            click_button('Sign up')

            click_button("post-id-delete")

            expect(page).to have_content "This is the first test post"
            expect(page).to have_content "You can only delete your own posts!"
        end
end