feature 'user can sign up' do

    scenario 'User can sign up' do

        visit '/'
        fill_in "name", with: "My name"
        fill_in "email", with: "email@me.com"
        fill_in "password", with: "password123"
        click_button('Sign up')
        expect(page).to have_content /Welcome to Acebook, My name/
   end

   scenario 'User can log in' do
        visit '/'

        fill_in "login-email", with: "email@me.com"
        fill_in "login-password", with: "password123"
        click_button('Log in')
        expect(page).to have_content /Welcome to Acebook, My name/
   end

    scenario 'User can sign out' do
        visit '/'

        fill_in "login-email", with: "email@me.com"
        fill_in "login-password", with: "password123"
        click_button('Log in')
        click_button('Sign out')
        expect(page).not_to have_content /Welcome to Acebook, My name/
   end

   scenario 'User can only sign up once' do
        visit '/'
        fill_in "name", with: "My name"
        fill_in "email", with: "email@me.com"
        fill_in "password", with: "password123"
        click_button('Sign up')
        expect(page).to have_content 'Email already exists, please try again'

    end

end