feature 'Creating and viewing comments' do

    scenario 'User can comment a post and view the comment' do
        visit '/'
        fill_in "name", with: 'user'
        fill_in "email", with: "email@me.com"
        fill_in "password", with: "password123"
        click_button('Sign up')

        visit('/posts')
        fill_in "post", with: 'This is the first test post'
        click_button("Post")
        fill_in "comment", with: "This is a test comment"
        click_button("Comment")
        expect(page).to have_content "This is a test comment"
    end

end

