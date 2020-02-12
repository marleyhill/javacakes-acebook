feature 'Creating and viewing a post' do

    scenario 'User can view post on posts page' do
        visit '/'
        fill_in "name", with: 'user'
        fill_in "email", with: "email@me.com"
        fill_in "password", with: "password123"
        click_button('Sign up')

        visit('/posts')
        fill_in :post, with: 'This is the first test post'
        click_button('Post')
        expect(page).to have_content 'This is the first test post'
    end


    scenario 'User can create and view post on posts page' do
       visit '/'
       fill_in "login-email", with: "email@me.com"
       fill_in "login-password", with: "password123"
       click_button('Log in')
       visit '/posts'
       fill_in :post, with: "This is another test post"
       click_button("Post")
       expect(page).to have_content "This is another test post"
    end
end