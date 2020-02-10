feature 'peeps are displayed in reverse chronological order' do
  scenario 'user can see latests posts first' do
    visit('/')
    fill_in :name, with: 'user'
    fill_in :email, with: "email@me.com"
    fill_in :password, with: "password123"
    click_button 'Submit'
    visit('/posts')
    fill_in :post, with: 'This is the second test'
    click_button 'Submit'
    expect(page).to have_content /.+?This is the second test .+? This is a test peep.+?/
  end
end